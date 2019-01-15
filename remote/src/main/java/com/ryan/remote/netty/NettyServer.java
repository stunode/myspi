package com.ryan.remote.netty;

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

    public NettyServer(int port) throws Exception {
        super(port);
    }

    @Override
    protected void doOpen() throws Throwable {
        bootstrap = new ServerBootstrap();
        workerGroup = new NioEventLoopGroup();
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true)); // (1)
        final NettyServerHandler serverHandler = new NettyServerHandler();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
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
