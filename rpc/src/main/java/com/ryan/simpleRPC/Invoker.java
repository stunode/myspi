package com.ryan.simpleRPC;

/**
 * 类名称: Invoker
 * 功能描述:
 * 日期:  2019/1/15 16:52
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface Invoker<T> {

    /**
     * 描述：get service interface
     * @author renpengfei
     * @since JDK 1.8
     */
    Class<T> getInterface();


    Object invoke(Invocation invocation);

}
