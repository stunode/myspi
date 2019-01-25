package rpc;

import com.ryan.common.serialization.FastjsonSerializer;
import com.ryan.common.serialization.Serializer;
import com.ryan.config.ServiceConfig;
import com.ryan.myspi.ExtensionLoader;
import com.ryan.simpleRPC.Protocol;
import com.ryan.simpleRPC.ServerHandler;
import com.ryan.simpleRPC.impl.DubboInvoker;
import com.ryan.simpleRPC.impl.DubboProtocol;
import com.ryan.simpleRPC.impl.MyClient;
import com.ryan.simpleRPC.impl.MyHandler;
import com.ryan.simpleRPC.impl.MyServer;
import com.ryan.simpleRPC.impl.RPCInvocation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

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

    @Test
    public void getDubboProtocol(){
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();
        Assert.assertEquals(DubboProtocol.class,protocol.getClass());
        DubboInvoker invoker = new DubboInvoker();
        protocol.export(invoker);
    }


    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8888);
                while (true) {
                    try {
                        RPCInvocation invocation = new RPCInvocation();
                        invocation.setMethodName("echo");
                        Serializer serializer = new FastjsonSerializer();
                        byte[] serialize = serializer.serialize(invocation);
                        System.out.println(serialize.length);
                        socket.getOutputStream().write(serialize);
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }
}
