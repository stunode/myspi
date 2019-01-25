package com.ryan.simpleRPC;

import com.ryan.SPI;

/**
 * 类名称: Protocol
 * 功能描述:
 * 日期:  2019/1/15 17:01
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("dubbo")
public interface Protocol {

    <T> Exporter<T> export(Invoker<T> invoker);

}
