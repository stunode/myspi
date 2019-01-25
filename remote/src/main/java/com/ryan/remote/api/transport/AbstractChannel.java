package com.ryan.remote.api.transport;

import com.ryan.remote.api.Channel;
import com.ryan.remote.api.ChannelHandler;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;

/**
 * 类名称: AbstractChannel
 * 功能描述:
 * 日期:  2019/1/25 13:53
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class AbstractChannel implements Channel {
    /**
     * get remote address.
     *
     * @return remote address.
     */
    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    /**
     * is connected.
     *
     * @return connected
     */
    @Override
    public boolean isConnected() {
        return false;
    }

    /**
     * has attribute.
     *
     * @param key key.
     * @return has or has not.
     */
    @Override
    public boolean hasAttribute(String key) {
        return false;
    }

    /**
     * get attribute.
     *
     * @param key key.
     * @return value.
     */
    @Override
    public Object getAttribute(String key) {
        return null;
    }

    /**
     * set attribute.
     *
     * @param key   key.
     * @param value value.
     */
    @Override
    public void setAttribute(String key, Object value) {

    }

    /**
     * remove attribute.
     *
     * @param key key.
     */
    @Override
    public void removeAttribute(String key) {

    }

    @Override
    public void send(Object message) throws RemoteException {

    }

    @Override
    public void close() {

    }

    @Override
    public ChannelHandler getChannelHandler() {
        return null;
    }
}
