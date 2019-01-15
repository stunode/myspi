package com.ryan.remote.api.transport;

import com.ryan.remote.api.Endpoint;
import com.ryan.remote.api.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Collection;
import java.util.Iterator;

/**
 * 类名称: AbstractPeer
 * 功能描述:
 * 日期:  2019/1/15 13:42
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public abstract class AbstractServer implements Endpoint, Server {

    protected int port;

    public AbstractServer(int port) throws Exception {
        try {
            this.port = port;
            doOpen();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception("Start bind server error");
        }
    }

    protected abstract void doOpen() throws Throwable ;

    protected abstract void doClose() throws Throwable;
    public void send(Object message) {
        // TODO
    }

    public void close() {

        // TODO

    }

}
