package com.ryan.myspi.adaptive.impl;

import com.ryan.common.URL;
import com.ryan.myspi.adaptive.Adap1;

/**
 * 类名称: Adap1Impl1
 * 功能描述:
 * 日期:  2018/12/16 13:34
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class Adap1Impl1 implements Adap1 {
    @Override
    public String echo(URL url) {
        return "adap1Impl1";
    }
}
