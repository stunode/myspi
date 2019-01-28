package rpc.serializer;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.common.serialization.Serializer;
import com.ryan.simpleRPC.impl.RpcInvocation;
import org.junit.Assert;
import org.junit.Test;

/**
 * 类名称: SerializerTest
 * 功能描述:
 * 日期:  2019/1/24 11:22
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class SerializerTest {

    @Test
    public void serializerTest() throws Exception{

        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setMethodName("serializerTest");


        Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getDefaultExtension();

        byte[] bytes = serializer.serialize(rpcInvocation);

        RpcInvocation rpcDesrializer = serializer.deserialize(RpcInvocation.class,bytes);

        Assert.assertEquals("serializerTest",rpcDesrializer.getMethodName());

        Assert.assertEquals(rpcInvocation,rpcDesrializer);


    }
}
