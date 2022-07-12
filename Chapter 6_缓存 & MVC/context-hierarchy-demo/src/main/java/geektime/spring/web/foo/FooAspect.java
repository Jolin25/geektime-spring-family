package geektime.spring.web.foo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * 切面
 *
 * @author Joly
 */
@Aspect
@Slf4j
public class FooAspect {
    // 这个切面拦截的是所有以 testBean 开头的bean
    @AfterReturning("bean(testBean*)")
    public void printAfter() {
        log.info("after hello()");
    }
}
