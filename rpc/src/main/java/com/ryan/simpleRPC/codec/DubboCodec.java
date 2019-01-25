package com.ryan.simpleRPC.codec;
import com.ryan.common.serialization.Serializer;
import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.Codec;
import com.ryan.remote.api.transport.CodecSupport;
import com.ryan.simpleRPC.Invocation;
import io.netty.buffer.ByteBuf;
import com.ryan.simpleRPC.impl.RPCInvocation;

/**
 * 类名称: DubboCodec
 * 功能描述:
 * 日期:  2019/1/16 18:17
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
// 编解码插件
public class DubboCodec implements Codec {


    @Override
    public void encode(Object message) {
        System.out.println(message);

    }

    @Override
    public Invocation decode(ByteBuf byteBuf) {
        // dubbo在这里优化了协议转换
        byte[] bytes = new byte[21];
        // 读取数据
        byteBuf.readBytes(bytes);
        // 反序列化
        return CodecSupport.getSerializer().deserialize(RPCInvocation.class,bytes);

    }
}
