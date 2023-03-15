package top.jolyne.spring0.scope;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author jrl
 * @date Create in 14:59 2022/12/16
 */
@Component
@Scope("prototype")
public class BeanA {
    public Integer age;
}
