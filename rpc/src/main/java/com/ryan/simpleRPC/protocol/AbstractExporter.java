package com.ryan.simpleRPC.protocol;

import com.ryan.simpleRPC.Exporter;
import com.ryan.simpleRPC.Invoker;

/**
 * 类名称: AbstractExporter
 * 功能描述:
 * 日期:  2019/1/25 17:32
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public abstract class AbstractExporter<T> implements Exporter<T> {

    private final Invoker<T> invoker;

    public AbstractExporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public String toString(){
        return getInvoker().toString();
    }
}
