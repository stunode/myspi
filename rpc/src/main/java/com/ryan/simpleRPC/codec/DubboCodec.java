package com.ryan.simpleRPC.codec;
import com.ryan.remote.api.Codec;
import com.ryan.remote.api.transport.CodecSupport;
import com.ryan.simpleRPC.Invocation;
import io.netty.buffer.ByteBuf;
import com.ryan.simpleRPC.impl.RpcInvocation;
import io.netty.buffer.ByteBufAllocator;

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
    public ByteBuf encode(Object message) {

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = CodecSupport.getSerializer().serialize(message);
        // 1.写入调用信息长度
        byteBuf.writeInt(bytes.length);
        // 2.写入调用信息
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    @Override
    public Invocation decode(ByteBuf byteBuf) {

        // 1.读取调用信息长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        // 2.读取调用信息
        byteBuf.readBytes(bytes);
        // 反序列化
        RpcInvocation invocation = null;
        try {
            invocation = CodecSupport.getSerializer().deserialize(RpcInvocation.class, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invocation;

    }
}
