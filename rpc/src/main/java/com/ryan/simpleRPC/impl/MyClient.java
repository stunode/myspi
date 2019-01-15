package com.ryan.simpleRPC.impl;

import com.ryan.simpleRPC.RPCClient;
import com.ryan.utils.StringUtils;

import java.lang.reflect.Proxy;

/**
 * 类名称: MyClient
 * 功能描述:
 * 日期:  2019/1/14 10:58
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class MyClient implements RPCClient {
    @Override
    public <T> T refer(Class<T> interfaceClass, String host, int port) {
        if (interfaceClass == null){
            throw new IllegalArgumentException("interfaceClass is null");
        }
        if (StringUtils.isEmpty(host)){
            throw new IllegalArgumentException("host is null");
        }
        if (port < 0 || port > 65535){
            throw new IllegalArgumentException("port is invalid, port is:" + port);
        }

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new MyRPCInvoker(host, port));

    }
}
