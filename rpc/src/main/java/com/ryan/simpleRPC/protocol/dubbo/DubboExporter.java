package com.ryan.simpleRPC.protocol.dubbo;

import com.ryan.simpleRPC.Invoker;
import com.ryan.simpleRPC.protocol.AbstractExporter;

/**
 * 类名称: DubboExporter
 * 功能描述:
 * 日期:  2019/1/25 17:41
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class DubboExporter<T> extends AbstractExporter<T> {

    private final String key;

    public DubboExporter(Invoker<T> invoker,String key) {
        super(invoker);
        this.key = key;
    }

}
