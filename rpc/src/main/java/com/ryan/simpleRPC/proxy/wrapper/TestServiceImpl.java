package com.ryan.simpleRPC.proxy.wrapper;

/**
 * 类名称: TestServiceImpl
 * 功能描述:
 * 日期:  2019/1/25 15:26
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class TestServiceImpl implements TestService {

    @Override
    public void echo() {
        System.out.println("this is test service impl");
    }
}
