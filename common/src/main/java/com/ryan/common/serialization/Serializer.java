package com.ryan.common.serialization;

import com.ryan.SPI;

/**
 * 类名称: Serialization
 * 功能描述:
 * 日期:  2019/1/24 11:05
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("fastjson")
public interface Serializer {

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);

}
