package com.ryan.myspi;

import com.ryan.SPI;

/**
 * 类名称: Protocal
 * 功能描述:
 * 日期:  2018/12/11 22:44
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("dubbo")
public interface Protocol {

    // 获取协议类型
    String getProtocolType();

    // 执行调用
    void invoke();
}
