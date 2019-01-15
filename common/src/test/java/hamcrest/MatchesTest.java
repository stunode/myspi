package hamcrest;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * 类名称: MatchesTest
 * 功能描述:
 * 日期:  2018/12/24 10:24
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
interface IHamcrest{

}

class HamcrestImpl1 implements IHamcrest {

    public IHamcrest hamcrest;

    public HamcrestImpl1(IHamcrest hamcrest) {
        this.hamcrest = hamcrest;
    }

    public HamcrestImpl1() {
    }
}

class HamcrestImpl2 implements IHamcrest {

}

class WrapperHamcrest implements IHamcrest {

    public IHamcrest hamcrest;
    public WrapperHamcrest(IHamcrest hamcrest) {
        this.hamcrest = hamcrest;
    }
}

public class MatchesTest {

    // 类型匹配，anyOf表示其中一个类型符合目标类型就通过
    @Test
    public void hamcrestMatch1(){
        HamcrestImpl2 impl2 = new HamcrestImpl2();
        Assert.assertThat(impl2,anyOf(instanceOf(HamcrestImpl2.class),instanceOf(IHamcrest.class),instanceOf(HamcrestImpl1.class)));
    }

    // allOf 表示 所有类型都要和目标类型匹配
    @Test
    public void hamcrestMatch2(){
        HamcrestImpl2 impl2 = new HamcrestImpl2();
        Assert.assertThat(impl2,allOf(instanceOf(HamcrestImpl2.class),instanceOf(IHamcrest.class),instanceOf(HamcrestImpl1.class)));
    }

    @Test
    public void testEquals() {
        HamcrestImpl1 ob1 = new HamcrestImpl1();
        HamcrestImpl1 ob2 = new HamcrestImpl1();
        // 测试异常
//        Assert.assertThat(ob1, equalTo(ob2));
        // 测试通过
//        Assert.assertEquals(ob1,ob1);
        Assert.assertThat(ob1, equalTo(ob1));
    }

    @Test
    public void wrapperTest(){
        try {
            // 获取IHamcrest类作为参数的public构造函数
            Constructor<WrapperHamcrest> constructor1 = WrapperHamcrest.class.getConstructor(IHamcrest.class);
            System.out.println(constructor1.getName());
            // 沒有无参构造函数，所以报错
            Constructor<WrapperHamcrest> constructor2 = WrapperHamcrest.class.getConstructor();
            System.out.println(constructor2.getName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
