package com.ryan.simpleRPC.impl;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.Channel;
import com.ryan.remote.api.Transporter;
import com.ryan.remote.api.transport.ChannelHandlerAdapter;
import com.ryan.simpleRPC.Exporter;
import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.Protocol;
import com.ryan.simpleRPC.ProxyFactory;
import com.ryan.simpleRPC.protocol.dubbo.DubboExporter;
import com.ryan.simpleRPC.proxy.wrapper.TestService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import javax.naming.event.ObjectChangeListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类名称: DubboProtocol
 * 功能描述:
 * 日期:  2019/1/15 17:03
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class DubboProtocol implements Protocol {

    private final ConcurrentMap<String, Transporter> serverMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();


    // 通过适配为ChannelInboundHandlerAdapter
    private ChannelHandlerAdapter requestHandler = new ChannelHandlerAdapter() {
        @Override
        public void received(Channel channel, Object message) throws Exception {

            if(message instanceof Invocation) {
                Invocation inv = (Invocation) message;

                String key = "";
                if ("echo".equals(inv.getMethodName())) {
                    key = "test";
                }
                Exporter<?> exporter = exporterMap.get(key);
                Invoker<?> ink = exporter.getInvoker();
                ink.invoke(inv);


                // TODO 进行反射调用,需要知道调用的类、参数类型、参数值、返回值
//                ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getDefaultExtension();
//                TestService testService = new com.ryan.simpleRPC.proxy.wrapper.TestServiceImpl();
//                Invoker<TestService> invoker = proxyFactory.getInvoker(testService, TestService.class);
//                RPCInvocation invocation = new RPCInvocation();
//                invocation.setMethodName("echo");
//                invoker.invoke(invocation);
//                exporterMap.get(inv.getMethodName())
            }
        }
    };

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {

        // 添加invoker
//        String key = serviceKey(invoker);
        String key = "test";
        DubboExporter<T> exporter = new DubboExporter<>(invoker,key);
        exporterMap.put(key, exporter);
        // 打开服务
        openServer();
        return exporter;
    }

    private void openServer() {
        Transporter transporter = serverMap.get("8888");
        if (transporter == null) {
            synchronized (this) {
                transporter = serverMap.get("8888");
                // 双重检查锁
                if (transporter == null) {
                    serverMap.put("8888",createServer());
                }
            }
        }
    }

    private Transporter createServer() {
        Transporter transporter = ExtensionLoader.getExtensionLoader(Transporter.class).getDefaultExtension();
        try {
            transporter.bind(8888,requestHandler);

//            transporter.bind(8888,requestHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transporter;
    }

    private <T> String serviceKey(Invoker<T> invoker) {
        return invoker.getInterface().getName();
    }

}
