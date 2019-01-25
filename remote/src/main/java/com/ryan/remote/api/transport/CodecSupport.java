package com.ryan.remote.api.transport;

import com.ryan.common.serialization.Serializer;
import com.ryan.myspi.ExtensionLoader;

/**
 * 类名称: CodecSupport
 * 功能描述: 避免Extension ioc 带来的循环依赖问题
 * 日期:  2019/1/24 13:59
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class CodecSupport {

    private static Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getDefaultExtension();

    private CodecSupport() {
    }

    public static Serializer getSerializer(){
        return serializer;
    }
}
