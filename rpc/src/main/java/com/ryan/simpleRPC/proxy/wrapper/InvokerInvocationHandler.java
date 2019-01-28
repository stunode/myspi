package com.ryan.simpleRPC.proxy.wrapper;

import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.impl.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 类名称: InvokerInvocationHandler
 * 功能描述:
 * 日期:  2019/1/25 15:17
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class InvokerInvocationHandler implements InvocationHandler {

    private final Invoker<?> invoker;

    public InvokerInvocationHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcInvocation invocation = new RpcInvocation(method, args);
        return invoker.invoke(invocation);
    }

}
