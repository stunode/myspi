package com.ryan.simpleRPC;

/**
 * 类名称: Exporter
 * 功能描述:
 * 日期:  2019/1/15 16:52
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();

}
