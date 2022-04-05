package top.joly.spring.circularreference.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.joly.spring.circularreference.spring.inter.Spring_A_Inter;
import top.joly.spring.circularreference.spring.inter.Spring_B_Inter;

/**
 * @author jrl
 * @date Create in 22:39 2022/4/5
 */
@Component
public class Spring_A implements Spring_A_Inter {
    @Autowired
    Spring_B_Inter spring_b;
}
