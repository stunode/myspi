package com.ryan.simpleRPC.impl;

import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;

import java.lang.reflect.Method;

/**
 * 类名称: DubboInvoker
 * 功能描述:
 * 日期:  2019/1/15 17:11
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class DubboInvoker<T> implements Invoker<T> {
    /**
     * 描述：get service interface
     *
     * @author renpengfei
     * @since JDK 1.8
     */
    @Override
    public Class<T> getInterface() {
        return null;
    }

    @Override
    public Object invoke(Invocation invocation) {
        //TODO
        return null;
    }
}
