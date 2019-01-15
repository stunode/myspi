package com.ryan.ext_wrapper;

import com.ryan.SPI;

/**
 * 类名称: CoreFunc
 * 功能描述:
 * 日期:  2018/12/24 12:03
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("impl")
public interface CoreFunc {

    String echo(String s);
}
