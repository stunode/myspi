package com.ryan.simpleRPC;

/**
 * 类名称: ServerHandler
 * 功能描述:
 * 日期:  2019/1/14 10:54
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface ServerHandler {

    public void handle(Object service , int port);
}
