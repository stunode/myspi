package com.ryan.myspi.adaptive;

import com.ryan.Adaptive;
import com.ryan.SPI;
import com.ryan.common.URL;

/**
 * 类名称: Adap1
 * 功能描述:
 * 日期:  2018/12/16 13:34
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI
public interface Adap1 {

    // 查找参数key1，来确定使用那个扩展实现类
    @Adaptive({"key1"})
    String echo(URL url);

}
