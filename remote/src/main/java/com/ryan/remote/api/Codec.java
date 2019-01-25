package com.ryan.remote.api;

import com.ryan.SPI;
import io.netty.buffer.ByteBuf;

import java.io.ObjectInputStream;

/**
 * 类名称: codec
 * 功能描述:
 * 日期:  2019/1/16 18:16
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@SPI("dubboCodec")
public interface Codec {

    void encode(Object message);

    Object decode(ByteBuf byteBuf);
}
