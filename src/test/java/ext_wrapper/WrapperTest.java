package ext_wrapper;

import com.ryan.common.URL;
import com.ryan.ext_wrapper.CoreFunc;
import com.ryan.myspi.ExtensionLoader;
import org.junit.Test;

/**
 * 类名称: WrapperTest
 * 功能描述:
 * 日期:  2018/12/24 12:14
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class WrapperTest {

    @Test
    public void wrapperTest(){
        CoreFunc impl = ExtensionLoader.getExtensionLoader(CoreFunc.class).getExtension("impl");
        String echo = impl.echo( "corefunc-impl");
    }
}
