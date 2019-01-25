package com.ryan.remote.api.exchange;

import com.ryan.remote.api.Channel;
import com.ryan.remote.api.ChannelHandler;

/**
 * 类名称: HeaderExchangerHandler
 * 功能描述:
 * 日期:  2019/1/16 17:11
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class HeaderExchangerHandler implements ExchangeHandler {
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
        System.out.println(message);
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
