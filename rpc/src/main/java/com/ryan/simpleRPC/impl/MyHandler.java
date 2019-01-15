package com.ryan.simpleRPC.impl;

import com.ryan.simpleRPC.ServerHandler;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 类名称: MyHandler
 * 功能描述:
 * 日期:  2019/1/14 10:58
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class MyHandler implements ServerHandler {
    @Override
    public void handle(Object service , int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            for (;;){
                //1.接收socket连接，当没有连接时会阻塞
                Socket socket = serverSocket.accept();
                //2.当接收连接时,使用新线程处理
                new Thread(new ServerTask(socket,service)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerTask implements Runnable{
        private final Socket socket;
        private final Object service;

        public ServerTask(Socket socket, Object service){
            this.socket = socket;
            this.service = service;
        }
        @Override
        public void run(){
            try {
                //处理连接,等待IO有数据
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //当有数据时,获取服务方法名称、方法参数、方法参数类型
                try {
                    // 协议层+消息解码
                    String methodName = objectInputStream.readUTF();
                    Class<?>[] parameterTypes = (Class<?>[])(objectInputStream.readObject());
                    Object[] arguments = (Object[])objectInputStream.readObject();

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    try {
                        // 反射调用本地方法
                        Method method = service.getClass().getMethod(methodName, parameterTypes);
                        Object result = method.invoke(service,arguments);
                        // 返回消息编码
                        objectOutputStream.writeObject(result);
                    }catch (Throwable e){
                        e.printStackTrace();
                    }finally {
                        objectOutputStream.close();
                    }
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }finally {
                    objectInputStream.close();
                }
            }catch (IOException e){

            }finally {
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
