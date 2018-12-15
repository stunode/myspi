package com.ryan.myspi;

import com.ryan.SPI;
import com.ryan.utils.Holder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类名称: ExtentionLoader
 * 功能描述:
 * 日期:  2018/12/12 18:19
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ExtensionLoader<T> {

    // 获取Extension class 的接口
    private Class<T> type;

    public ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    // 已经通过final赋值，所以不需要进行使用volatile进行指令重排序
    // final 保证引用的对象不可被修改,代码可以被别人修改，这个字段相当于一个协议，不可随意修改引用
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<Map<String, Class<?>>> ();
    // 存放class实现类的缓存
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<Class<?>, Object> ();

    private static final String FILE_PATH_PREFIX = "META-INF/services/";

    // 默认类名
    private String defaultName;

    // 判断是否SPI接口
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent (SPI.class);
    }

    public T getExtension(Class<T> type,String name) {

        if(!withExtensionAnnotation (type)){
            throw new IllegalArgumentException (" type WITHOUT @" + SPI.class.getSimpleName () + " Annotation ");
        }

        if (name == null || name == "") {
            throw new IllegalArgumentException (" getExtension params name == null ");
        }
        try {
            Class<?> clazz = getExtensionClasses(type).get(name);

            // 查缓存
            T instance = (T) EXTENSION_INSTANCES.get (clazz);
            if(instance==null){
                EXTENSION_INSTANCES.putIfAbsent (clazz, clazz.newInstance ());
                instance = (T) EXTENSION_INSTANCES.get (clazz);
            }
            return instance;
        } catch (Throwable e) {
            throw new IllegalArgumentException (" clazz " + type + " newInstance error");
        }
    }

    public Class<?> getExtensionClass(Class<T> type, String name) {
        if (name == null || name.length () == 0) {
            throw new IllegalArgumentException ("getExtension params name == null");
        }
        Map<String, Class<?>> classes = getExtensionClasses (type);
        return classes.get (name);

    }

    private Map<String, Class<?>> getExtensionClasses(Class<T> type) {

        Map<String, Class<?>> classes = cachedClasses.get ();
        // dubbo-check
        if (classes == null) {
            // 加锁，防止重复创建
            synchronized (cachedClasses) {
                classes = cachedClasses.get ();
                if(classes==null) {
                    classes = loadExtensionClasses (type);
                }
                cachedClasses.set (classes);
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses(Class<T> type) {
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
//        String value = Protocol.class.getAnnotation (SPI.class).value ();
//        System.out.println (value);
//        String fileName = "META-INF/services/" + DubboProtocol.class.getName ();
//        System.out.println (fileName);
        ExtensionLoader<Protocol> extensionLoader = new ExtensionLoader<> (Protocol.class);
        Protocol dubbo = extensionLoader.getExtension (Protocol.class, "dubbo");
        dubbo.invoke ();

    }

}
