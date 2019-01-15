package com.ryan.utils;

/**
 * 类名称: Holder
 * 功能描述:
 * 日期:  2018/12/13 15:55
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class Holder<T> {

    // 保证可见性
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
