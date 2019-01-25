package com.ryan.remote.netty;

import com.ryan.remote.api.transport.AbstractChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 类名称: DiscardServerHandler
 * 功能描述:
 * 日期:  2019/1/15 13:22
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private com.ryan.remote.api.ChannelHandler ch;

    public NettyServerHandler(com.ryan.remote.api.ChannelHandler ch) {
        this.ch = ch;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        try {
            ch.received(new AbstractChannel(),msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
