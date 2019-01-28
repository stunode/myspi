package com.ryan.simpleRPC.proxy;

import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;

/**
 * 类名称: AbstractProxyInvoker
 * 功能描述:
 * 日期:  2019/1/25 16:18
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private final T proxy;

    private final Class<T> type;

    public AbstractProxyInvoker(T proxy, Class<T> type) {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy == null");
        }
        if (type == null) {
            throw new IllegalArgumentException("interface == null");
        }
        if (!type.isInstance(proxy)) {
            throw new IllegalArgumentException(proxy.getClass().getName() + " not implement interface " + type);
        }
        this.proxy = proxy;
        this.type = type;
    }

    /**
     * 描述：get service interface
     *
     * @author renpengfei
     * @since JDK 1.8
     */
    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public Object invoke(Invocation invocation) {
        Object obj = null;
        try {
            obj = doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;


}
