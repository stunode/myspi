package com.ryan.simpleRPC.proxy;

import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.ProxyFactory;
import com.ryan.simpleRPC.proxy.wrapper.InvokerInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 类名称: JdkProxyFactory
 * 功能描述:
 * 日期:  2019/1/25 15:14
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
//
public class JdkProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),interfaces, new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type) {
        return new AbstractProxyInvoker<T>(proxy,type) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }


}
