package com.ryan.remote.netty;

import com.ryan.remote.api.Channel;
import com.ryan.remote.api.ChannelHandler;
import com.ryan.remote.api.Server;
import com.ryan.remote.api.transport.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 类名称: NettyServer
 * 功能描述:
 * 日期:  2019/1/15 14:23
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class NettyServer extends AbstractServer implements Server {

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public NettyServer(int port, ChannelHandler handler) throws Exception {
        super(port,handler);
    }

    @Override
    protected void doOpen() throws Throwable {
        bootstrap = new ServerBootstrap();
        workerGroup = new NioEventLoopGroup();
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true)); // (1)
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), NettyServer.this);
                            // 绑定编解码
                            ch.pipeline().addLast("decoder", adapter.getDecoder());
                            // 绑定handler
                            ch.pipeline().addLast(new NettyServerHandler(getChannelHandler()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            ChannelFuture f = bootstrap.bind(this.port).sync(); // (7)
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (bootstrap!=null) {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
