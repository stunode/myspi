package com.ryan.simpleRPC;

/**
 * 类名称: RPCClient
 * 功能描述:
 * 日期:  2019/1/14 10:50
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface RPCClient {

    public <T> T refer(Class<T> interfaceClass, String host, int port);
}
