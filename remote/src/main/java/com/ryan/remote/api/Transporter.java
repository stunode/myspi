package com.ryan.remote.api;

import com.ryan.SPI;

/**
 * 类名称: Transporter
 * 功能描述:
 * 日期:  2019/1/15 14:52
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("netty")
public interface Transporter {

    Server bind(int port) throws Exception ;

    // Client connect
}
