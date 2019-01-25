package com.ryan.remote.netty;

import com.ryan.remote.api.ChannelHandler;
import com.ryan.remote.api.Server;
import com.ryan.remote.api.Transporter;

/**
 * 类名称: NettyTransporter
 * 功能描述:
 * 日期:  2019/1/15 14:53
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class NettyTransporter implements Transporter {



    @Override
    public Server bind(int port, ChannelHandler listener) throws Exception {
        return new NettyServer(port,listener);
    }



}
