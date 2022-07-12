package top.joly.spring.circularreference.jvm;

/**
 * @author jrl
 * @date Create in 20:59 2022/4/5
 */
public class JVM_B {
    JVM_A jvm_a;
    public JVM_B (){
        this.jvm_a = new JVM_A();
    }
}
