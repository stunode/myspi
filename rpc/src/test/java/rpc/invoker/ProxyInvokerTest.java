package rpc.invoker;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.ProxyFactory;
import com.ryan.simpleRPC.impl.RpcInvocation;
import com.ryan.simpleRPC.proxy.wrapper.TestService;
import com.ryan.simpleRPC.proxy.wrapper.TestServiceImpl;
import org.junit.Test;

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
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName("echo");
        invocation.setInvoker(invoker);
        invoker.invoke(invocation);

    }
}
