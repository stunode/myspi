package rpc.invoker;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.simpleRPC.Invocation;
import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.ProxyFactory;
import com.ryan.simpleRPC.impl.DubboInvoker;
import com.ryan.simpleRPC.impl.RPCInvocation;
import com.ryan.simpleRPC.proxy.JdkProxyFactory;
import com.ryan.simpleRPC.proxy.wrapper.TestService;
import com.ryan.simpleRPC.proxy.wrapper.TestServiceImpl;
import org.junit.Test;
import rpc.ITestService;

import java.lang.reflect.Method;

/**
 * 类名称: InvokerTest
 * 功能描述:
 * 日期:  2019/1/25 16:05
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ProxyInvokerTest {

    // 反射调用
    @Test
    public void getProxyTest(){
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getDefaultExtension();
        TestService testService = new com.ryan.simpleRPC.proxy.wrapper.TestServiceImpl();
        Invoker<TestService> invoker = proxyFactory.getInvoker(testService, TestService.class);
        RPCInvocation invocation = new RPCInvocation();
        invocation.setMethodName("echo");
        invoker.invoke(invocation);

    }
    

    @Test
    public void invokeTest() throws Exception {

        Method method = TestService.class.getMethod("echo");
        TestService testService = new TestServiceImpl();
        method.invoke(testService, null);
    }
}
