package com.ryan.ext_activate;

import com.ryan.SPI;

/**
 * 类名称: ExportListener
 * 功能描述:
 * 日期:  2018/12/25 17:06
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI
public interface ExportListener {

    String echo(String s);
}
