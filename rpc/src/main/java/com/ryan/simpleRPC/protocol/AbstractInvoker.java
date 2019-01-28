package com.ryan.simpleRPC.protocol;

import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.impl.RpcInvocation;

/**
 * 类名称: AbstractInvoker
 * 功能描述:
 * 日期:  2019/1/25 14:51
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {

    public Object invoke(Invocation inv){

        RpcInvocation invocation = (RpcInvocation) inv;
        return doInvoke(invocation);

    }

    abstract Object doInvoke(RpcInvocation invocation);

}
