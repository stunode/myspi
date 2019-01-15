package com.ryan.ext_wrapper;

/**
 * 类名称: CoreFuncTransactionWrapper
 * 功能描述:
 * 日期:  2018/12/24 12:09
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class CoreFuncTransactionWrapper implements CoreFunc {

    private CoreFunc coreFunc;

    public CoreFuncTransactionWrapper(CoreFunc coreFunc) {
        this.coreFunc = coreFunc;
    }

    @Override
    public String echo(String s) {
        // 前置增强
        System.out.println(" commit begin ");

        return coreFunc.echo(s);
    }
}
