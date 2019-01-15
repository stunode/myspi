package com.ryan.myspi.ext1.impl;

import com.ryan.myspi.Protocol;
import com.ryan.myspi.ext1.Ext1;

/**
 * 类名称: Ext1Impl1
 * 功能描述:
 * 日期:  2018/12/15 22:15
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class Ext1Impl2 implements Ext1 {

    private Protocol dubbo;

    public void setDubbo(Protocol protocol) {
        this.dubbo = protocol;
    }

    @Override
    public String echo() {
        dubbo.invoke ();
        return "Ext1Impl1-echo";
    }
}
