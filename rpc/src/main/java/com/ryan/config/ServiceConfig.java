package com.ryan.config;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.Protocol;
import com.ryan.simpleRPC.ProxyFactory;
import com.ryan.simpleRPC.impl.DubboInvoker;
import com.ryan.simpleRPC.proxy.wrapper.TestService;

/**
 * 类名称: ServiceConfig
 * 功能描述:
 * 日期:  2019/1/15 16:43
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ServiceConfig<T> {

    private String interfaceName;

    private Class<?> interfaceClass;

    private T ref;

    // 使用的Protocol通过SPI获取单例对象
    private static final Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();

    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getDefaultExtension();

    public synchronized void doExport() {

        TestService testService = new com.ryan.simpleRPC.proxy.wrapper.TestServiceImpl();
        Invoker<TestService> invoker = proxyFactory.getInvoker(testService, TestService.class);
        protocol.export(invoker);

    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }


}
