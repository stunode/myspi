package rpc;

import com.ryan.config.ServiceConfig;
import org.junit.Test;

/**
 * 类名称: ServiceConfigTesgt
 * 功能描述:
 * 日期:  2019/1/15 17:55
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ServiceConfigTest {

    @Test
    public void serviceConfig_doExport(){

        ServiceConfig serviceConfig = new ServiceConfig();

        serviceConfig.setInterfaceClass(ITestService.class);
        serviceConfig.setRef(new TestServiceImpl());

        serviceConfig.doExport();
    }
}
