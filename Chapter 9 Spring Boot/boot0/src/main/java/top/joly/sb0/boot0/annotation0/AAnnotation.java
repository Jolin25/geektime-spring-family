package top.joly.sb0.boot0.annotation0;

import java.lang.annotation.Annotation;

/**
 * @author jrl
 * @date Create in 20:05 2022/12/5
 */

/**
 * knowledge point:  自定义注解只能 extends Annotation接口，不可以 implements。
 * 所以就被默认 extends Annotation接口了，不过为了表明我这个是个注解，所以用的 @interface。
 */
public @interface AAnnotation {
    // TODO_Joly:非法。
    //  那Annotation里的 toString方法 留给谁实现？
    // @Override
    // default String toString(){
    //
    // }
}
