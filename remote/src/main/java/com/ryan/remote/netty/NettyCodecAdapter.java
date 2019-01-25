package com.ryan.remote.netty;

import com.ryan.remote.api.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.List;

/**
 * 类名称: NettyCodecAdapter
 * 功能描述:
 * 日期:  2019/1/16 18:27
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class NettyCodecAdapter {

    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    private final Codec codec;

    public NettyCodecAdapter(Codec codec,com.ryan.remote.api.ChannelHandler handler) {
        this.codec = codec;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    private class InternalEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            // TODO

            System.out.println(msg);
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
            // 解码+反序列化
            Object msg = codec.decode(input);
            if (msg != null) {
                out.add(msg);
            }
        }
    }
}
