package com.ryan.myspi.support;

import com.ryan.SPI;

/**
 * 类名称: ExtensionFactory
 * 功能描述:
 * 日期:  2018/12/20 11:56
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI
public interface ExtensionFactory {
    <T> T getExtension(Class<T> type, String name);
}
