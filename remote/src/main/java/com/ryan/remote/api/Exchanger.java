package com.ryan.remote.api;

import com.ryan.remote.api.exchange.ExchangeHandler;

/**
 * 类名称: Exchanger
 * 功能描述:
 * 日期:  2019/1/16 15:38
 *
 * @author: renpengfei
 * @since: JDK1.8
 */

public interface Exchanger {

    ExchangeServer bind(ExchangeHandler handler) throws Exception;

}
