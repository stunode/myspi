package com.ryan.ext_wrapper;

/**
 * 类名称: CoreFuncImpl
 * 功能描述:
 * 日期:  2018/12/24 12:03
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class CoreFuncImpl implements CoreFunc {

    @Override
    public String echo( String s) {
        return "core-func-impl echo : " + s;
    }
}
