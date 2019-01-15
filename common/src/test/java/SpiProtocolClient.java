import com.ryan.common.URL;
import com.ryan.ext_activate.ExportListener;
import com.ryan.myspi.ExtensionLoader;
import com.ryan.myspi.Protocol;
import com.ryan.myspi.adaptive.SimpleExt;
import com.ryan.myspi.ext1.Ext1;
import org.junit.Assert;
import org.junit.Test;
import sun.rmi.transport.Transport;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 类名称: SpiProtocolClient
 * 功能描述:
 * 日期:  2018/12/11 22:57
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class SpiProtocolClient {

    private final static Map<String,Protocol> RPOTOCOL_LIST = new HashMap<> ();

//    static {
//        // 使用ServiceLoader
//        ServiceLoader<Protocol> serviceLoader = ServiceLoader.load (Protocol.class);
//        Iterator<Protocol> iterator = serviceLoader.iterator ();
//        while (iterator.hasNext ()) {
//            Protocol itr = iterator.next ();
//            RPOTOCOL_LIST.put (itr.getProtocolType (), itr);
//        }
//    }

    /**
     * 描述：调用相关协议
     * @author renpengfei
     * @since JDK 1.8
     */
    public void doInvoke(String protocolType) {
        Protocol protocol = SpiProtocolClient.RPOTOCOL_LIST.get (protocolType);
        if (protocol != null) {
            protocol.invoke ();
        }
    }

    @Test
    public void invokeExtension(){
        Protocol protocol = ExtensionLoader.getExtensionLoader (Protocol.class).getExtension ("dubbo");
        String echo = protocol.invoke ();
        assertEquals("dubbo_protocol", echo);
    }

    @Test
    public void injectExtension(){
        Ext1 ext1 = ExtensionLoader.getExtensionLoader (Ext1.class).getExtension ("ext1Impl1");
        assertEquals ("hessian_protocol",ext1.echo ());
    }

    @Test
    public void getAdaptiveExtension(){
        SimpleExt ext = ExtensionLoader.getExtensionLoader(SimpleExt.class).getAdaptiveExtension();

        Map<String, String> map = new HashMap<String, String>();
        map.put("key2", "impl2");
        URL url = new URL("p1", "1.2.3.4", 1010, "path1", map);

        String echo = ext.yell(url, "haha");
        assertEquals("Ext1Impl2-yell", echo);

        url = url.addParameter("key1", "impl3"); // note: URL is value's type
        echo = ext.yell(url, "haha");
        assertEquals("Ext1Impl3-yell", echo);
    }

    @Test
    public void getActivateExtensions(){
        List<ExportListener> exts = ExtensionLoader.getExtensionLoader(ExportListener.class).getActivateExtensions();
        Assert.assertEquals(2,exts.size());
    }

}
