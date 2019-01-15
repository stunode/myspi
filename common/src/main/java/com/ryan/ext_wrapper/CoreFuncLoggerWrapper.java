package com.ryan.ext_wrapper;

/**
 * 类名称: CoreFuncTimerWrapper
 * 功能描述:
 * 日期:  2018/12/24 12:04
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class CoreFuncLoggerWrapper implements CoreFunc {

    CoreFunc coreFunc;

    public CoreFuncLoggerWrapper(CoreFunc coreFunc) {
        this.coreFunc = coreFunc;
    }

    @Override
    public String echo(String s) {

        // 前置增强
        System.out.println(" core func execute ");

        return coreFunc.echo(s);
    }
}
