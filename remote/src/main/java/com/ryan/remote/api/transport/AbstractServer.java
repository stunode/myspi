package com.ryan.remote.api.transport;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.Channel;
import com.ryan.remote.api.ChannelHandler;
import com.ryan.remote.api.Codec;
import com.ryan.remote.api.Endpoint;
import com.ryan.remote.api.Server;

/**
 * 类名称: AbstractPeer
 * 功能描述:
 * 日期:  2019/1/15 13:42
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public abstract class AbstractServer implements Endpoint, Server ,ChannelHandler{

    private final ChannelHandler handler;

    private final Codec codec;

    protected int port;

    public AbstractServer(int port, ChannelHandler handler) throws Exception {

        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }

        try {
            this.handler = handler;
            this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getDefaultExtension();
            this.port = port;
            doOpen();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception("Start bind server error");
        }
    }


    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    public void send(Object message) {
        // TODO
    }

    public void close() {

        // TODO

    }

    public ChannelHandler getChannelHandler() {
        return handler;
    }

    protected Codec getCodec() {
        return codec;
    }

    /**
     * on channel connected.
     *
     * @param channel channel.
     */
    @Override
    public void connected(Channel channel) throws Exception {

    }

    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    @Override
    public void disconnected(Channel channel) throws Exception {

    }

    /**
     * on message sent.
     *
     * @param channel channel.
     * @param message message.
     */
    @Override
    public void sent(Channel channel, Object message) throws Exception {

    }

    /**
     * on message received.
     *
     * @param channel channel.
     * @param message message.
     */
    @Override
    public void received(Channel channel, Object message) throws Exception {

        // TODO
        System.out.println("abstract server received message");
    }

    /**
     * on exception caught.
     *
     * @param channel   channel.
     * @param exception exception.
     */
    @Override
    public void caught(Channel channel, Throwable exception) throws Exception {

    }
}
