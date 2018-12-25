package com.ryan.ext_activate;

/**
 * 类名称: ExportListenerImpl2
 * 功能描述:
 * 日期:  2018/12/25 17:08
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ExportListenerImpl2 implements ExportListener {
    @Override
    public String echo(String s) {
        return "exportListenerImpl2";
    }
}
