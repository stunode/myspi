package com.ryan.remote.api;

/**
 * 类名称: Endpoint
 * 功能描述:
 * 日期:  2019/1/15 13:35
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface Endpoint {

    void send(Object message);

    void close();
}
