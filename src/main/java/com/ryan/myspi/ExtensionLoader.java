package com.ryan.myspi;

import com.ryan.Adaptive;
import com.ryan.SPI;
import com.ryan.common.URL;
import com.ryan.myspi.support.ExtensionFactory;
import com.ryan.utils.ConcurrentHashSet;
import com.ryan.utils.Holder;
import com.ryan.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * 类名称: ExtentionLoader
 * 功能描述:
 * 日期:  2018/12/12 18:19
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ExtensionLoader<T> {

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    // 扩展点实现 工厂类，当依赖注入扩展点时，通过objectFactory获取对应的扩展点
    // 同时也是一个扩展点，包括获取Spring，和SPI扩展点以及自定义扩展点
    // 使用IOC注入扩展点的依赖，而不直接依赖ExtensionLoader工厂类
    private final ExtensionFactory objectFactory;
    // 获取Extension class 的接口
    private final Class<T> type;

    // 构造函数改为私有
    private ExtensionLoader(Class<T> type) {
        this.type = type;
        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }

    // 已经通过final赋值，所以不需要进行使用volatile进行指令重排序
    // final 保证引用的对象不可被修改,代码可以被别人修改，这个字段相当于一个协议，不可随意修改引用
    // 非static修饰，每个ExtensionLoader保存一份扩展类的副本
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<Map<String, Class<?>>>();

    // 存放class实现类的缓存
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<Class<?>, Object>();

    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    // 持有自适应扩展实现类对象
    private final Holder<Object> cachedAdaptiveInstance = new Holder<Object>();

    private volatile Throwable createAdaptiveInstanceError = null;

    // 对引用的改变保证内存可见性，初始值为null
    private volatile Class<?> cachedAdaptiveClass = null;

    // 不包含自适应扩展点实现类
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<String, Holder<Object>>();

    // AOP包装类型集合,有可能不存在，不用初始化
    private Set<Class<?>> cachedWrapperClasses;

    private static final String FILE_PATH_PREFIX = "META-INF/services/";

    private String cachedDefaultName;

    // 判断是否SPI接口
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    // 通过静态工厂获取ExtensionLoader
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        // 参数校验
        if (type == null) {
            throw new IllegalArgumentException(" getExtensionLoader param type == null");
        }
        // 服务接口校验
        if (!type.isInterface()) {
            throw new IllegalArgumentException(" extension type " + type + " is not interface! ");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException(" type WITHOUT @" + SPI.class.getSimpleName() + " Annotation ");
        }

        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            // 通过增加参数type，确定ExtensionLoader对象的服务类型
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    // 不包含自适应扩展点实现类
    public T getExtension(String name) {

        if (name == null || name == "") {
            throw new IllegalArgumentException(" getExtension param name == null ");
        }
        // 通过ConcurrentMap保证线程安全
        // 使用Holder来保证扩展实现对象的可见性
        // Holder类中的value引用可以被改变
        Holder<Object> objectHolder = cachedInstances.get(name);
        if (objectHolder == null) {
            // 线程同时到时，ConcurrentMap保证线程安全性，使得拿到的objectHolder为同一个对象
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            objectHolder = cachedInstances.get(name);
        }

        Object instance = objectHolder.get();
        if (instance == null) {
            synchronized (objectHolder) {
                instance = objectHolder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    objectHolder.set(instance);
                }
            }
        }
        return (T) instance;

    }

    @SuppressWarnings("unchecked")
    public T getAdaptiveExtension() {
        // 创建两个缓存，一个保存创建成功的自适应扩展点实现类，一个保存失败后的异常信息
        // 查找缓存
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            // 创建异常缓存，避免失败后重复创建，浪费系统资源
            if (createAdaptiveInstanceError == null) {
                synchronized (cachedAdaptiveInstance) {
                    instance = cachedAdaptiveInstance.get();
                    if (instance == null) {
                        try {
                            // 缓存为空，则新增createAdaptiveExtension()
                            instance = createAdaptiveExtension();
                            // 设置缓存
                            cachedAdaptiveInstance.set(instance);
                        } catch (Throwable t) {
                            // 捕获异常，将异常添加到缓存；再抛出,防止吞掉异常
                            createAdaptiveInstanceError = t;
                            throw new IllegalStateException("fail to create adaptive instance: " + t.toString(), t);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("fail to create adaptive instance: " + createAdaptiveInstanceError.toString(), createAdaptiveInstanceError);
            }
        }

        return (T) instance;
    }

    // 新增经过注入的扩展类
    @SuppressWarnings("unchecked")
    public T createExtension(String name) {

        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new IllegalStateException("get extension classes error , name is : " + name);
        }

        try {
            // 查缓存
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            injectExtension(instance);
            // 如果存在wrapperClass，进行包装
            if (cachedWrapperClasses != null) {
                for (Class<?> wrapperClass : cachedWrapperClasses) {
                    // 注入
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable e) {
            throw new IllegalArgumentException(" clazz " + type + " newInstance error");
        }
    }

    public Set<String> getSupportedExtensions() {
        Map<String, Class<?>> clazzes = getExtensionClasses();
        return Collections.unmodifiableSet(new TreeSet<String>(clazzes.keySet()));
    }

    public T getDefaultExtension() {
        getExtensionClasses();
        if (null == cachedDefaultName || cachedDefaultName.length() == 0
                || "true".equals(cachedDefaultName)) {
            return null;
        }
        return getExtension(cachedDefaultName);
    }

    private T injectExtension(T instance) {
        if (instance == null) {
            throw new IllegalArgumentException(" injectExtension param instance == null");
        }

        // 通过反射注入扩展类
        for (Method method : instance.getClass().getMethods()) {
            if (method.getName().startsWith("set")
                    && method.getParameterTypes().length == 1
                    && Modifier.isPublic(method.getModifiers())) {
                // 注入
                Class<?> pt = method.getParameterTypes()[0];
                // 获取参数
                try {
                    String methodName = method.getName();
                    String property = methodName.length() > 3 ? methodName.substring(3, 4).toLowerCase() + methodName.substring(4) : "";
//                    Object ob = ExtensionLoader.getExtensionLoader (pt).getExtension (property);
                    Object ob = objectFactory.getExtension(pt, property);
                    // 反射调用set方法注入该扩展类
                    if (ob != null) {
                        method.invoke(instance, ob);
                    }
                } catch (Exception e) {
                    System.out.println("fail to inject via method :" + method.getName());
                }
            }

        }
        return instance;

    }

    public Class<?> getExtensionClass(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("getExtension param name == null");
        }
        Map<String, Class<?>> classes = getExtensionClasses();
        return classes.get(name);

    }

    private Map<String, Class<?>> getExtensionClasses() {

        // cachedClasses是一个Holder对象,
        Map<String, Class<?>> classes = cachedClasses.get();
        // dubbo-check
        if (classes == null) {
            // 加锁，防止重复创建
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                }
                cachedClasses.set(classes);
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        // 为后续代码维护者提供一个信息，该注解的对象引用不可被改变
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);

        // 获取默认扩展实现类别名，值可以有一个默认扩展实现类别名
        if (defaultAnnotation != null) {
            // defaultName默认扩展实现类别名
            String value = defaultAnnotation.value();
            if ((value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if (names.length > 1) {
                    throw new IllegalStateException("more than 1 default extension name on extension " + type.getName()
                            + ": " + Arrays.toString(names));
                }
                if (names.length == 1) {
                    cachedDefaultName = names[0];
                }
            }
        }
        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();

        // fileName = 根路径+接口全限定名
        String fileName = "META-INF/services/" + type.getName();
        // 读取fileName里的资源

        Enumeration<java.net.URL> urls;
        ClassLoader classLoader = findClassLoader();

        try {
            // 加载配置资源，URL表示资源位置
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    // loadResource ，加载资源，重头戏来了
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return extensionClasses;
    }

    private ClassLoader findClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    // 加载资源
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "utf-8"));
            // 读取配置
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        String name;
                        String[] split = line.split("=");
                        name = split[0];
                        line = split[1];
                        // 加载类
                        loadClass(extensionClasses, Class.forName(line, true, classLoader), name);
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {

        // 判断是类上是否有Adaptive注解，如果包含Adaptive注解，则加入cachedAdaptiveClass缓存
        // Dubbo 目前只有两个扩展点使用类上Adaptive注解，Compile 和 ExtensionFactory
        if (clazz.isAnnotationPresent(Adaptive.class)) {
            if (cachedAdaptiveClass == null) {
                cachedAdaptiveClass = clazz;
            } else {
                // 判断唯一性
                if (cachedAdaptiveClass != clazz) {
                    throw new IllegalStateException("AdaptiveClass is redundance : " + cachedAdaptiveClass.getClass().getName());
                }
            }
            // 添加包装类型
        } else if (isWrapperClass(clazz)) {
            if (cachedWrapperClasses == null) {
                cachedWrapperClasses = new ConcurrentHashSet<Class<?>>();
            }
            cachedWrapperClasses.add(clazz);
        } else {
            extensionClasses.put(name, clazz);
        }
    }

    private boolean isWrapperClass(Class<?> clazz) {
        try {
            clazz.getConstructor(type);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private T createAdaptiveExtension() {
        try {
            // newInstance只可以调用无参的构造函数，如果无法创建对象，会抛出InstantiationException异常
            return injectExtension((T) getAdaptiveExtensionClass().newInstance());
        } catch (Exception e) {
            throw new IllegalStateException(" Can not create adaptive extension  " + type + ", cause: " + e.getMessage(), e);
        }
    }

    private Class<?> getAdaptiveExtensionClass() {
        // 获取所有扩展点
        getExtensionClasses();
        // 如果是Compile服务或者是ExtensionFactory服务，那么在获取所有扩展点时，缓存中放入了Adaptive扩展点
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        // 其他扩展点调用createAdaptiveExtensionClass生成AdaptiveClass
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }

    // 生成自适应扩展点实现类
    private Class<?> createAdaptiveExtensionClass() {
        // 生成适配类的字节码
        // 通过判断方法中是否有Adaptive注解
        // 方法中没有Adaptive注解，直接抛出异常
        String code = createAdaptiveExtensionClassCode();
        ClassLoader classLoader = findClassLoader();
        com.ryan.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(com.ryan.compiler.Compiler.class).getAdaptiveExtension();
        return compiler.compile(code, classLoader);
    }

    // 生成自适应扩展点实现类class code
    private String createAdaptiveExtensionClassCode() {
        StringBuilder codeBuilder = new StringBuilder();
        Method[] methods = type.getMethods();
        boolean hasAdaptiveAnnotation = false;
        // 如果方法含有Adaptive注解，就生成Adaptive Class
        for (Method m : methods) {
            if (m.isAnnotationPresent(Adaptive.class)) {
                hasAdaptiveAnnotation = true;
                break;
            }
        }
        // no need to generate adaptive class since there's no adaptive method found.
        if (!hasAdaptiveAnnotation) {
            throw new IllegalStateException("No adaptive method on extension " + type.getName() + ", refuse to create the adaptive class!");
        }

        // 包名和服务类的包名相同
        codeBuilder.append("package ").append(type.getPackage().getName()).append(";");
        codeBuilder.append("\nimport ").append(ExtensionLoader.class.getName()).append(";");
        codeBuilder.append("\npublic class ").append(type.getSimpleName()).append("$Adaptive").append(" implements ").append(type.getCanonicalName()).append(" {");

        codeBuilder.append("\nprivate java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger(0);\n");

        for (Method method : methods) {
            Class<?> rt = method.getReturnType();
            Class<?>[] pts = method.getParameterTypes();
            Class<?>[] ets = method.getExceptionTypes();

            Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
            StringBuilder code = new StringBuilder(512);
            // 如果方法没有Adaptive注解，抛出异常
            if (adaptiveAnnotation == null) {
                code.append("throw new UnsupportedOperationException(\"method ")
                        .append(method.toString()).append(" of interface ")
                        .append(type.getName()).append(" is not adaptive method!\");");
            } else {
                int urlTypeIndex = -1;
                // 查找URL参数
                for (int i = 0; i < pts.length; ++i) {
                    if (pts[i].equals(URL.class)) {
                        urlTypeIndex = i;
                        break;
                    }
                }
                // 存在URL类型的参数
                if (urlTypeIndex != -1) {
                    // 判断语句，如果参数为空，抛异常
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"url == null\");",
                            urlTypeIndex);
                    code.append(s);
                    // 生成临时变量url
                    s = String.format("\n%s url = arg%d;", URL.class.getName(), urlTypeIndex);
                    code.append(s);
                }
                // did not find parameter in URL type
                // 不存在URL类型的参数，则判断是否存在UrlHolder(也可以是其他带有返回值为Url的get方法的参数)
                else {
                    String attribMethod = null;

                    // find URL getter method
                    LBL_PTS:
                    for (int i = 0; i < pts.length; ++i) {
                        // 获取参数中方法
                        Method[] ms = pts[i].getMethods();
                        for (Method m : ms) {
                            String name = m.getName();
                            // 参数中的方法包含返回URL类型的get方法
                            if ((name.startsWith("get") || name.length() > 3)
                                    && Modifier.isPublic(m.getModifiers())
                                    && !Modifier.isStatic(m.getModifiers())
                                    && m.getParameterTypes().length == 0
                                    && m.getReturnType() == URL.class) {
                                urlTypeIndex = i;
                                attribMethod = name;
                                // 退出LBL_PTS标注的循环体
                                break LBL_PTS;
                            }
                        }
                    }
                    // 如果所有参数都不包含返回URL的方法，抛出异常
                    if (attribMethod == null) {
                        throw new IllegalStateException("fail to create adaptive class for interface " + type.getName()
                                + ": not found url parameter or url attribute in parameters of method " + method.getName());
                    }

                    // Null point check
                    // 空指针校验
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"%s argument == null\");",
                            urlTypeIndex, pts[urlTypeIndex].getName());
                    code.append(s);
                    s = String.format("\nif (arg%d.%s() == null) throw new IllegalArgumentException(\"%s argument %s() == null\");",
                            urlTypeIndex, attribMethod, pts[urlTypeIndex].getName(), attribMethod);
                    code.append(s);

                    // URL 变量 赋值为 参数中的attribMethod方法返回值
                    s = String.format("%s url = arg%d.%s();", URL.class.getName(), urlTypeIndex, attribMethod);
                    code.append(s);
                }

                String[] value = adaptiveAnnotation.value();
                // value is not set, use the value generated from class name as the key
                if (value.length == 0) {
                    // 驼峰名分割 比如 SimpleExt 分割为 simple.ext
                    String splitName = StringUtils.camelToSplitName(type.getSimpleName(), ".");
                    value = new String[]{splitName};
                }
                // 判断参数中是否有Invocation类型，如果有 Dubbo rpc调用，methodName等于Invocation类的getMethodName方法
                boolean hasInvocation = false;
                for (int i = 0; i < pts.length; ++i) {
                    if (("org.apache.dubbo.rpc.Invocation").equals(pts[i].getName())) {
                        // Null Point check
                        String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"invocation == null\");", i);
                        code.append(s);
                        s = String.format("\nString methodName = arg%d.getMethodName();", i);
                        code.append(s);
                        hasInvocation = true;
                        break;
                    }
                }

                // 默认扩展实现类名
                String defaultExtName = cachedDefaultName;
                String getNameCode = null;
                for (int i = value.length - 1; i >= 0; --i) {
                    if (i == value.length - 1) {
                        if (null != defaultExtName) {

                            if (!"protocol".equals(value[i])) {
                                // 如果不是Protocol服务，判断是否有Invocation参数
                                if (hasInvocation) {
                                    // rpc调用
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                } else {
                                    // 非RPC调用
                                    getNameCode = String.format("url.getParameter(\"%s\", \"%s\")", value[i], defaultExtName);
                                }
                            } else {
                                getNameCode = String.format("( url.getProtocol() == null ? \"%s\" : url.getProtocol() )", defaultExtName);
                            }
                        } else {
                            if (!"protocol".equals(value[i])) {
                                if (hasInvocation) {
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                } else {
                                    // 非RPC调用，如果defaultExtName为null，则不穿该参数
                                    getNameCode = String.format("url.getParameter(\"%s\")", value[i]);
                                }
                            } else {
                                getNameCode = "url.getProtocol()";
                            }
                        }
                    } else {
                        if (!"protocol".equals(value[i])) {
                            if (hasInvocation) {
                                getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                            } else {
                                getNameCode = String.format("url.getParameter(\"%s\", %s)", value[i], getNameCode);
                            }
                        } else {
                            getNameCode = String.format("url.getProtocol() == null ? (%s) : url.getProtocol()", getNameCode);
                        }
                    }
                }
                // URL参数规则，驼峰名以"." 号进行分隔，做为key
                code.append("\nString extName = ").append(getNameCode).append(";");
                // check extName == null?
                String s = String.format("\nif(extName == null) " +
                                "throw new IllegalStateException(\"Fail to get extension(%s) name from url(\" + url.toString() + \") use keys(%s)\");",
                        type.getName(), Arrays.toString(value));
                code.append(s);
                // 获取扩展实现类
                code.append(String.format("\n%s extension = null;\n try {\nextension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);\n}catch(Exception e){\n",
                        type.getName(), ExtensionLoader.class.getSimpleName(), type.getName()));
                // 使用count表示，只打印一次warn日志
//                code.append (String.format ("if (count.incrementAndGet() == 1) {\nSystem.out.println (\"Failed to find extension named \" + extName + \" for type %s, will use default extension %s instead.\", e);\n}\n",
//                        type.getName (), defaultExtName));
                code.append(String.format("extension = (%s)%s.getExtensionLoader(%s.class).getExtension(\"%s\");\n}",
                        type.getName(), ExtensionLoader.class.getSimpleName(), type.getName(), defaultExtName));

                // return statement
                if (!rt.equals(void.class)) {
                    code.append("\nreturn ");
                }

                s = String.format("extension.%s(", method.getName());
                code.append(s);
                for (int i = 0; i < pts.length; i++) {
                    if (i != 0) {
                        code.append(", ");
                    }
                    code.append("arg").append(i);
                }
                code.append(");");
            }

            codeBuilder.append("\npublic ").append(rt.getCanonicalName()).append(" ").append(method.getName()).append("(");
            for (int i = 0; i < pts.length; i++) {
                if (i > 0) {
                    codeBuilder.append(", ");
                }
                codeBuilder.append(pts[i].getCanonicalName());
                codeBuilder.append(" ");
                codeBuilder.append("arg").append(i);
            }
            codeBuilder.append(")");
            if (ets.length > 0) {
                codeBuilder.append(" throws ");
                for (int i = 0; i < ets.length; i++) {
                    if (i > 0) {
                        codeBuilder.append(", ");
                    }
                    codeBuilder.append(ets[i].getCanonicalName());
                }
            }
            codeBuilder.append(" {");
            codeBuilder.append(code.toString());
            codeBuilder.append("\n}");
        }
        codeBuilder.append("\n}");
        return codeBuilder.toString();
    }

}
