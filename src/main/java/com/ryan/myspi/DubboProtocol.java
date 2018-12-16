package com.ryan.myspi;

/**
 * 类名称: DubboProtocol
 * 功能描述:
 * 日期:  2018/12/11 22:49
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class DubboProtocol implements Protocol {

    private static final String protocolType = "dubbo";

    @Override
    public String getProtocolType() {
        return protocolType;
    }

    @Override
    public String invoke() {
        System.out.println ("this is dubbo protocol ");
        return "dubbo_protocol";
    }
}
