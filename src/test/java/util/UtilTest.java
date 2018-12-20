package util;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名称: UtilTest
 * 功能描述:
 * 日期:  2018/12/17 10:35
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class UtilTest {

    @Test
    public void autoIncrementTest() {
        String s = String.format ("\nif(arg%d == null)", 2);
        System.out.println (s);
    }

    @Test
    public void breakLabeled(){

        first:
        for (int i = 0; i < 10; i++) {
            second:
            for (int j = 0; j < 5; j++) {
                System.out.println (j);
                if(j==3) {
                    // 退出second循环
                    break second;
                }
            }
            System.out.println (i);
            if (i == 5) {
                // 退出first循环
                break first;
            }
        }
        System.out.println (" quit circulation ");
    }

    @Test
    public void autoIntegerTest(){
        AtomicInteger count = new AtomicInteger (0);
        if(count.incrementAndGet() == 1){
            System.out.println (1);
        } else {
            System.out.println (0);
        }
    }

    @Test
    public void regexTest(){

        Pattern pattern = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");
        String target = "package org.apache.dubbo.common.extension.extension.ext1;";
        Matcher matcher = pattern.matcher(target);
        String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        System.out.println (pkg);
    }


    @Test
    public void testNewInstance(){
        try {
            ConstructTest instance = ConstructTest.class.newInstance ();
//            WithoutNoParamsConstruct test = new WithoutNoParamsConstruct ();
            instance.test ();
        } catch (Throwable e) {
            System.out.println (e.getMessage ());
        }
    }
}
