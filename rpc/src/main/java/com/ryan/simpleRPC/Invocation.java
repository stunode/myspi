package com.ryan.simpleRPC;

import com.ryan.common.serialization.Serializer;

/**
 * 类名称: Invocation
 * 功能描述:
 * 日期:  2019/1/15 16:55
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public interface Invocation {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();

}
