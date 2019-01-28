package rpc;

import com.ryan.common.serialization.FastjsonSerializer;
import com.ryan.common.serialization.Serializer;
import com.ryan.config.ServiceConfig;
import com.ryan.simpleRPC.ServerHandler;
import com.ryan.simpleRPC.codec.DubboCodec;
import com.ryan.simpleRPC.impl.MyClient;
import com.ryan.simpleRPC.impl.MyHandler;
import com.ryan.simpleRPC.impl.MyServer;
import com.ryan.simpleRPC.impl.RpcInvocation;
import com.ryan.simpleRPC.proxy.wrapper.TestService;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 类名称: MyServerTest
 * 功能描述:
 * 日期:  2019/1/14 11:06
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class MyServerTest {

    @Test
    public void serverExport(){
        ServerHandler serverHandler = new MyHandler();

        MyServer myServer = new MyServer(serverHandler);
        myServer.setPort(6666);
//        myServer.setService(new TestServiceImpl());
        myServer.export();

    }

    @Test
    public void clientRefer(){
        MyClient client = new MyClient();
//        ITestService testService = client.refer(ITestService.class, "127.0.0.1", 6666);

//        testService.rpcEcho();
    }

    @Test
    public void serviceConfig(){

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.doExport();

    }


    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8888);
                while (true) {
                    try {
                        RpcInvocation invocation = new RpcInvocation();
                        invocation.setMethodName("echo");
                        invocation.setInterfaceType(TestService.class);
                        Serializer serializer = new FastjsonSerializer();
                        byte[] serialize = serializer.serialize(invocation);

                        DubboCodec dubboCodec = new DubboCodec();
                        ByteBuf encodeByteBuf = dubboCodec.encode(invocation);
                        int length = encodeByteBuf.readableBytes();
                        System.out.println(length);
                        byte[] dataBytes = new byte[length];
                        encodeByteBuf.readBytes(dataBytes);

                        socket.getOutputStream().write(dataBytes);
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }
}
