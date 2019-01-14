package com.ryan.simpleRPC.impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 类名称: MyRPCInvoker
 * 功能描述:
 * 日期:  2019/1/14 11:12
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class MyRPCInvoker implements InvocationHandler {
    private String host;

    private int port;

    public MyRPCInvoker(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket(host, port);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 协议层+消息编码
            objectOutputStream.writeUTF(method.getName());
            objectOutputStream.writeObject(method.getParameterTypes());
            objectOutputStream.writeObject(args);
            //等待服务端返回数据
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            try {
                // 服务调用返回结果解码
                Object result = objectInputStream.readObject();
                if (result instanceof Throwable){
                    throw (Throwable) result;
                }
                return result;
            }catch (Throwable e){
                e.printStackTrace();
            }finally {
                objectInputStream.close();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }finally {
            socket.close();
        }
        return null;
    }
}
