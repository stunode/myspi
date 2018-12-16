package com.ryan.myspi;

import com.ryan.SPI;
import com.ryan.myspi.ext1.impl.Ext1Impl1;
import com.ryan.utils.Holder;
import org.omg.CORBA.OBJ_ADAPTER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * 类名称: ExtentionLoader
 * 功能描述:
 * 日期:  2018/12/12 18:19
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ExtensionLoader<T> {

    // 构造函数改为私有
    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    // 获取Extension class 的接口
    private Class<T> type;

    // 已经通过final赋值，所以不需要进行使用volatile进行指令重排序
    // final 保证引用的对象不可被修改,代码可以被别人修改，这个字段相当于一个协议，不可随意修改引用
    // 非static修饰，每个ExtensionLoader保存一份扩展类的副本
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<Map<String, Class<?>>> ();

    // 存放class实现类的缓存
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<Class<?>, Object> ();

    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<> ();

    // 经过注入的扩展实现类缓存
    private final ConcurrentMap<String, Holder<Object>> injectedCachedInstances = new ConcurrentHashMap<String, Holder<Object>>();


    private static final String FILE_PATH_PREFIX = "META-INF/services/";

    // 默认类名
    private String defaultName;

    // 判断是否SPI接口
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent (SPI.class);
    }

    // 通过静态工厂获取ExtensionLoader
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        // 参数校验
        if (type == null) {
            throw new IllegalArgumentException (" getExtensionLoader param type == null");
        }
        // 服务接口校验
        if (!type.isInterface ()) {
            throw new IllegalArgumentException (" extension type " + type + " is not interface! ");
        }
        if (!withExtensionAnnotation (type)) {
            throw new IllegalArgumentException (" type WITHOUT @" + SPI.class.getSimpleName () + " Annotation ");
        }

        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get (type);
        if (extensionLoader == null) {
            // 通过增加参数type，确定ExtensionLoader对象的服务类型
            EXTENSION_LOADERS.putIfAbsent (type, new ExtensionLoader<T> (type));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get (type);
        }
        return extensionLoader;
    }

    public T getExtension(String name) {

        if (name == null || name == "") {
            throw new IllegalArgumentException (" getExtension param name == null ");
        }
        // 通过ConcurrentMap保证线程安全
        // 使用Holder来保证扩展实现对象的可见性
        // Holder类中的value引用可以被改变
        Holder<Object> objectHolder = injectedCachedInstances.get (name);
        if (objectHolder == null) {
            // 线程同时到时，ConcurrentMap保证线程安全性，使得拿到的objectHolder为同一个对象
            injectedCachedInstances.putIfAbsent (name,new Holder<Object> ());
            objectHolder = injectedCachedInstances.get (name);
        }

        Object instance = objectHolder.get ();
        if (instance == null) {
            synchronized (objectHolder) {
                Object o = objectHolder.get ();
                if (o == null) {
                    objectHolder.set (createExtension (name));
                    instance = objectHolder.get ();
                }
            }
        }
        return (T) instance;

    }

    // 新增经过注入的扩展类
    public T createExtension(String name) {
        if (name == null || name == "") {
            throw new IllegalArgumentException (" getExtension param name == null ");
        }
        try {
            Class<?> clazz = getExtensionClasses ().get (name);

            // 查缓存
            T instance = (T) EXTENSION_INSTANCES.get (clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent (clazz, clazz.newInstance ());
                instance = (T) EXTENSION_INSTANCES.get (clazz);
            }
            injectExtension (instance);
            return instance;
        } catch (Throwable e) {
            throw new IllegalArgumentException (" clazz " + type + " newInstance error");
        }
    }

    private T injectExtension(T instance) {
        if (instance == null) {
            throw new IllegalArgumentException (" injectExtension param instance == null");
        }

        // 通过反射注入扩展类
        for (Method method : instance.getClass ().getMethods ()) {
            if (method.getName ().startsWith ("set")
                    && method.getParameterTypes ().length == 1
                    && Modifier.isPublic (method.getModifiers ())) {
                // 注入
                Class<?> pt = method.getParameterTypes ()[0];
                // 获取参数
                try {
                    String methodName = method.getName ();
                    String property = methodName.length ()>3 ? methodName.substring (3, 4).toLowerCase () + methodName.substring (4) : "";
                    // 获取type为property的扩展类
                    Object ob = ExtensionLoader.getExtensionLoader (pt).getExtension (property);
                    // 反射调用set方法注入该扩展类
                    if(ob!=null) {
                        method.invoke (instance, ob);
                    }
                } catch (Exception e) {
                    System.out.println ("fail to inject via method :" + method.getName ());
                }
            }

        }
        return instance;

    }

    public Class<?> getExtensionClass(String name) {
        if (name == null || name.length () == 0) {
            throw new IllegalArgumentException ("getExtension param name == null");
        }
        Map<String, Class<?>> classes = getExtensionClasses ();
        return classes.get (name);

    }

    private Map<String, Class<?>> getExtensionClasses() {

        // cachedClasses是一个Holder对象,
        Map<String, Class<?>> classes = cachedClasses.get ();
        // dubbo-check
        if (classes == null) {
            // 加锁，防止重复创建
            synchronized (cachedClasses) {
                classes = cachedClasses.get ();
                if (classes == null) {
                    classes = loadExtensionClasses ();
                }
                cachedClasses.set (classes);
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        // 为后续代码维护者提供一个信息，该注解的对象引用不可被改变
        final SPI defaultAnnotation = type.getAnnotation (SPI.class);
        if (defaultAnnotation != null) {
            String value = defaultAnnotation.value ();
            defaultName = value;
        }
        Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>> ();

        // fileName = 根路径+接口全限定名
        String fileName = "META-INF/services/" + type.getName ();
        // 读取fileName里的资源

        Enumeration<URL> urls;
        ClassLoader classLoader = findClassLoader ();

        try {
            // 加载配置资源，URL表示资源位置
            urls = classLoader.getResources (fileName);
            if (urls != null) {
                while (urls.hasMoreElements ()) {
                    java.net.URL resourceURL = urls.nextElement ();
                    // loadResource ，加载资源，重头戏来了
                    loadResource (extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace ();
        }

        return extensionClasses;
    }

    private ClassLoader findClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread ().getContextClassLoader ();
        } catch (Exception e) {
            ClassLoader.getSystemClassLoader ();
        }
        return classLoader;
    }

    // 加载资源
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL) {

        try {
            BufferedReader reader = new BufferedReader (new InputStreamReader (resourceURL.openStream (), "utf-8"));
            // 读取配置
            try {
                String line;
                while ((line = reader.readLine ()) != null) {
                    line = line.trim ();
                    if (line.length () > 0) {
                        String name;
                        String[] split = line.split ("=");
                        name = split[0];
                        line = split[1];
                        // 加载类
                        loadClass (extensionClasses, Class.forName (line, true, classLoader), name);
                    }
                }
            } finally {
                reader.close ();
            }
        } catch (Throwable e) {
            e.printStackTrace ();
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
        extensionClasses.put (name, clazz);
    }

    public static void main(String[] args) {
//        Protocol protocol = ExtensionLoader.getExtensionLoader (Protocol.class).getExtension ("dubbo");
//        protocol.invoke ();
        Method[] methods = Ext1Impl1.class.getMethods ();
        for (Method method : methods) {
            System.out.println (method.getName ());
            System.out.println (Modifier.isPublic (method.getModifiers ()));
        }
    }

}
