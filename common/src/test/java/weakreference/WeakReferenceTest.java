package weakreference;

import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 类名称: WeakReferenceTest
 * 功能描述:
 * 日期:  2018/12/21 13:16
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class WeakReferenceTest {

    // 软引用测试
    @Test
    public void test1(){
        SoftReference softProduct = new SoftReference(new Product());
        System.gc();
        Assert.assertNotEquals(null,softProduct.get());
    }

    // 弱引用测试
    @Test
    public void test2(){
        WeakReference weakProduct = new WeakReference(new Product());
        System.gc();
        Assert.assertEquals(null,weakProduct.get());
    }

    // 强引用
    @Test
    public void test3(){
        Product product = new Product();
        System.gc();
        Assert.assertNotEquals(null,product);
    }

    // 弱引用通常在Map中使用
}
