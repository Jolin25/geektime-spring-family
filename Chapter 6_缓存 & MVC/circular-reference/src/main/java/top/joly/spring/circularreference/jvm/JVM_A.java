package top.joly.spring.circularreference.jvm;

/**
 * @author jrl
 * @date Create in 20:59 2022/4/5
 */
public class JVM_A {
    JVM_B jvm_b;

    public JVM_A() {
        this.jvm_b = new JVM_B();
    }
}
