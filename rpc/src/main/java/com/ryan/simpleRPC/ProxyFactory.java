package com.ryan.simpleRPC;

import com.ryan.SPI;

/**
 * 类名称: ProxyFactory
 * 功能描述:反射调用方法
 * 日期:  2019/1/25 15:07
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("jdk")
public interface ProxyFactory {

    <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces);

    <T> Invoker<T> getInvoker(T proxy, Class<T> type);
}
