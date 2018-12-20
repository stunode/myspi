package util;

/**
 * 类名称: ConstructTest
 * 功能描述:
 * 日期:  2018/12/19 11:04
 *
 * @author: renpengfei
 * @since: JDK1.8
 */
public class ConstructTest {
    private String name;

    public ConstructTest(String name) {
        this.name = name;
    }

    public ConstructTest() {
    }

    public void test(){
            System.out.println (name);
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
}
