package netty;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.Transporter;
import com.ryan.remote.netty.NettyTransporter;
import org.junit.Assert;
import org.junit.Test;
import sun.nio.ch.Net;
import sun.rmi.transport.Transport;

/**
 * 类名称: SpiTest
 * 功能描述:
 * 日期:  2019/1/15 15:54
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class SpiTest {

    @Test
    public void getTransporter() {

        Transporter transport = ExtensionLoader.getExtensionLoader(Transporter.class).getDefaultExtension();
        Assert.assertEquals(NettyTransporter.class, transport.getClass());

    }
}
