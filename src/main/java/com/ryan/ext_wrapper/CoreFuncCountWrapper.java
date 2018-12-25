package com.ryan.ext_wrapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名称: CoreFuncTimerWrapper
 * 功能描述:
 * 日期:  2018/12/24 12:04
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class CoreFuncCountWrapper implements CoreFunc {
    private CoreFunc coreFunc;
    // 扩展点构造函数，因此可以认定这个类为CoreFunc扩展点Wrapper类
    public CoreFuncCountWrapper(CoreFunc coreFunc) {
        this.coreFunc = coreFunc;
    }

    // 统计调用次数
    public static AtomicInteger echoCount = new AtomicInteger();
    @Override
    public String echo(String s) {
        // 前置增强
        echoCount.incrementAndGet();
        return coreFunc.echo(s);
    }
}
