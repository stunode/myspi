//package com.ryan.myspi.adaptive.impl;
//
//import com.ryan.Adaptive;
//import com.ryan.myspi.ExtensionLoader;
//
//@Adaptive
//public class SimpleExt$Adaptive implements com.ryan.myspi.adaptive.SimpleExt {
//
//    public java.lang.String yell(com.ryan.common.URL arg0, java.lang.String arg1) {
//        if (arg0 == null) throw new IllegalArgumentException ("url == null");
//        com.ryan.common.URL url = arg0;
//        String extName = url.getParameter ("key1", url.getParameter ("key2", "impl1"));
//        com.ryan.myspi.adaptive.SimpleExt extension = null;
//        try {
//            extension = (com.ryan.myspi.adaptive.SimpleExt) ExtensionLoader.getExtensionLoader (com.ryan.myspi.adaptive.SimpleExt.class).getExtension (extName);
//        } catch (exception e) {
//            extension = (com.ryan.myspi.adaptive.SimpleExt) ExtensionLoader.getExtensionLoader (com.ryan.myspi.adaptive.SimpleExt.class).getExtension ("impl1");
//        }
//        return extension.yell (arg0, arg1);
//    }
//
//    public java.lang.String echo(com.ryan.common.URL arg0, java.lang.String arg1) {
//        if (arg0 == null) throw new IllegalArgumentException ("url == null");
//        com.ryan.common.URL url = arg0;
//        String extName = url.getParameter ("simple.ext", "impl1");
//        com.ryan.myspi.adaptive.SimpleExt extension = null;
//        try {
//            extension = (com.ryan.myspi.adaptive.SimpleExt) ExtensionLoader.getExtensionLoader (com.ryan.myspi.adaptive.SimpleExt.class).getExtension (extName);
//        } catch (exception e) {
//            extension = (com.ryan.myspi.adaptive.SimpleExt) ExtensionLoader.getExtensionLoader (com.ryan.myspi.adaptive.SimpleExt.class).getExtension ("impl1");
//        }
//        return extension.echo (arg0, arg1);
//    }
//
//    public java.lang.String bang(com.ryan.common.URL arg0, int arg1) {
//        throw new UnsupportedOperationException ("method public abstract java.lang.String com.ryan.myspi.adaptive.SimpleExt.bang(com.ryan.common.URL,int) of interface com.ryan.myspi.adaptive.SimpleExt is not adaptive method!");
//    }
//}
