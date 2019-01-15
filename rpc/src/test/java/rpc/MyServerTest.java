package rpc;

import com.ryan.simpleRPC.ServerHandler;
import com.ryan.simpleRPC.impl.MyClient;
import com.ryan.simpleRPC.impl.MyHandler;
import com.ryan.simpleRPC.impl.MyServer;
import org.junit.Test;

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
}
