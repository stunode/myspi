package com.ryan.simpleRPC.impl;

import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 类名称: RpcInvocation
 * 功能描述:
 * 日期:  2019/1/24 11:21
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class RpcInvocation implements Invocation, Serializable {


    private static final long serialVersionUID = 1904229059335019972L;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Invoker<?> invoker;

    private Class<?> interfaceType;

    public RpcInvocation() {
    }

    public RpcInvocation(Method method, Object[] arguments) {
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.arguments = arguments;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Invoker<?> getInvoker() {
        return invoker;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }
}
