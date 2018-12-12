import com.ryan.myspi.Protocol;

import java.util.*;

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

    static {
        // 使用ServiceLoader
        ServiceLoader<Protocol> serviceLoader = ServiceLoader.load (Protocol.class);
        Iterator<Protocol> iterator = serviceLoader.iterator ();
        while (iterator.hasNext ()) {
            Protocol itr = iterator.next ();
            RPOTOCOL_LIST.put (itr.getProtocolType (), itr);
        }
    }

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

    public static void main(String[] args) {

        SpiProtocolClient spiProtocolClient = new SpiProtocolClient ();
        spiProtocolClient.doInvoke ("dubbo");
        spiProtocolClient.doInvoke ("hessian");
    }
}
