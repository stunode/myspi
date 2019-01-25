package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 类名称: NettyModuleTest
 * 功能描述:
 * 日期:  2019/1/18 17:24
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class NettyModuleTest {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("netty in action", Charset.forName("UTF-8"));

        String s = buf.toString(Charset.forName("UTF-8"));
        System.out.println(s);
        char c = buf.readChar();
        System.out.println(c);

    }
}
