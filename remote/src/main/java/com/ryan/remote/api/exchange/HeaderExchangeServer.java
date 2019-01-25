package com.ryan.remote.api.exchange;

import com.ryan.remote.api.ChannelHandler;
import com.ryan.remote.api.ExchangeServer;
import com.ryan.remote.api.Server;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 类名称: HeaderExchangeServer
 * 功能描述:
 * 日期:  2019/1/16 15:54
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class HeaderExchangeServer implements ExchangeServer {

    private final Server server;

    private AtomicBoolean closed = new AtomicBoolean(false);

    public HeaderExchangeServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("server == null");
        }
        this.server = server;
    }

    @Override
    public void send(Object message) throws RemoteException {

        if(closed.get()){
            throw new RemoteException("Failed to send message!");
        }
        server.send(message);
    }

    @Override
    public void close() {

    }

    @Override
    public ChannelHandler getChannelHandler() {
        return null;
    }
}
