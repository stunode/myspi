package com.ryan.common.serialization;

import com.alibaba.fastjson.JSON;
import com.ryan.common.serialization.Serializer;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;

/**
 * 类名称: FastjsonSerializer
 * 功能描述:
 * 日期:  2019/1/24 11:09
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class FastjsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws Exception {
        return JSON.parseObject(bytes, clazz);
    }

}
