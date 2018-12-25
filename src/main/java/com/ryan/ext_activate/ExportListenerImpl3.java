package com.ryan.ext_activate;

import com.ryan.Activate;

/**
 * 类名称: ExportListenerImpl1
 * 功能描述:
 * 日期:  2018/12/25 17:07
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
@Activate
public class ExportListenerImpl3 implements ExportListener {

    @Override
    public String echo(String s) {
        return "exportListenerImpl3";
    }
}
