package top.joly.spring.circularreference.jvm;

/**
 * 测试循环依赖（也不算JVM层的，应该算是java代码层的）
 * @author jrl
 * @date Create in 20:59 2022/4/5
 */
public class JVM_test {
    public static void main(String[] args) {
        JVM_A jvm_a = new JVM_A();
        System.out.println(jvm_a);
        System.out.println(jvm_a.jvm_b);
    }
}
