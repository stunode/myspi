package com.ryan.myspi;

/**
 * 类名称: HessianProtocol
 * 功能描述:
 * 日期:  2018/12/11 22:50
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class HessianProtocol implements Protocol {

    private static final String protocolType = "hessian";

    @Override
    public String getProtocolType() {
        return protocolType;
    }

    @Override
    public String invoke() {
        System.out.println ("this is hessian protocol ");
        return "hessian_protocol";
    }
}
