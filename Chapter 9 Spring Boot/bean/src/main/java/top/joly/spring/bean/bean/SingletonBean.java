package top.joly.spring.bean.bean;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author jrl
 * @date Create in 8:28 2022/3/22
 */
@Data
@Scope("singleton")
@Component
public class SingletonBean {
    String name;
}
