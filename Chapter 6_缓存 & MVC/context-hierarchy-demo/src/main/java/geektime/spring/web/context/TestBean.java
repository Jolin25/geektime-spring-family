package geektime.spring.web.context;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 要增强的目标:就是一个普通的类
 *
 * @author Joly
 */
@AllArgsConstructor
@Slf4j
public class TestBean {
    /**
     * 用于说明是哪个上下文来增强这个类的
     */
    private String context;

    public void hello() {
        log.info("hello " + context);
    }
}
