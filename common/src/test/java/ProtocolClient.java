import com.ryan.myspi.DubboProtocol;
import com.ryan.myspi.HessianProtocol;
import com.ryan.myspi.Protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名称: ProtocolClient
 * 功能描述:
 * 日期:  2018/12/11 23:20
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ProtocolClient {

    private final static Map<String,Protocol> RPOTOCOL_LIST = new HashMap<> ();

    static {
        RPOTOCOL_LIST.put ("dubbo", new DubboProtocol ());
        RPOTOCOL_LIST.put ("hessian", new HessianProtocol ());
    }

    public void doInvoke(String protocolType) {
        Protocol protocol = ProtocolClient.RPOTOCOL_LIST.get (protocolType);
        if (protocol != null) {
            protocol.invoke ();
        }
    }
}
