package com.ryan.remote.api.exchange;

import com.ryan.myspi.ExtensionLoader;
import com.ryan.remote.api.ExchangeServer;
import com.ryan.remote.api.Exchanger;
import com.ryan.remote.api.Transporter;

/**
 * 类名称: HeaderExchange
 * 功能描述:
 * 日期:  2019/1/16 17:14
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class HeaderExchanger implements Exchanger {
    @Override
    public ExchangeServer bind(ExchangeHandler handler) throws Exception{
        Transporter transporter = ExtensionLoader.getExtensionLoader(Transporter.class).getDefaultExtension();
        return new HeaderExchangeServer(transporter.bind(8888,handler));
    }
}
