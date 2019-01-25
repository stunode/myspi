package com.ryan.simpleRPC.impl;

import com.ryan.simpleRPC.Invocation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 类名称: RPCInvocation
 * 功能描述:
 * 日期:  2019/1/24 11:21
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class RPCInvocation implements Invocation, Serializable {


    private static final long serialVersionUID = 1904229059335019972L;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    public RPCInvocation() {
    }

    public RPCInvocation(Method method, Object[] arguments) {
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

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
