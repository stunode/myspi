package com.ryan.simpleRPC.impl;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.Transporter;
import com.ryan.remote.netty.NettyTransporter;
import com.ryan.simpleRPC.ServerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类名称: RPCServer
 * 功能描述:
 * 日期:  2019/1/14 10:56
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class MyServer implements com.ryan.simpleRPC.RPCServer {

    private ServerHandler serverHandler;

    private Object service;

    private int port;

    // serverhandler 缓存
    private ConcurrentMap<Object,ServerHandler> serverHandlerCache = new ConcurrentHashMap<>();

    public MyServer(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public MyServer(ServerHandler serverHandler, Object service, int port) {
        this.serverHandler = serverHandler;
        this.service = service;
        this.port = port;
        serverHandlerCache.putIfAbsent(service, serverHandler);
    }

    @Override
    public synchronized void export() {

//        if (service == null) {
//            throw new IllegalArgumentException("service is null");
//        }
//        if (port < 0 || port > 65535) {
//            throw new IllegalArgumentException("port is invalid, port is:" + port);
//        }
//        System.out.println("export service :" + service.getClass().getName() + "on port:" + port);
//
//        serverHandler.handle(service, port);

        Transporter transporter = ExtensionLoader.getExtensionLoader(Transporter.class).getDefaultExtension();
        try {
            transporter.bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
