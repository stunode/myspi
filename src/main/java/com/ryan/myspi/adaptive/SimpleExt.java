package com.ryan.myspi.adaptive;

import com.ryan.Adaptive;
import com.ryan.SPI;
import com.ryan.common.URL;

/**
 * 类名称: SimpleExt
 * 功能描述:
 * 日期:  2018/12/18 22:04
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("impl1")
public interface SimpleExt {

    @Adaptive
    String echo(URL url, String s);

    @Adaptive({"key1", "key2"})
    String yell(URL url, String s);

    // no @Adaptive
    String bang(URL url, int i);
}
