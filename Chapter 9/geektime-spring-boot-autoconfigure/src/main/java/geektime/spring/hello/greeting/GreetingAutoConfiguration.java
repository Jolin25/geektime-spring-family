package geektime.spring.hello.greeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在有 greeting jar 包并且配置中开启了这个jar并且还没有这个bean的情况下，
 * 向上下文中注册 GreetingApplicationRunner 的 Bean
 *
 * @author jrl
 * @date 2022/4/18
 */
@Slf4j
@Configuration
@ConditionalOnClass(GreetingApplicationRunner.class)
public class GreetingAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(GreetingApplicationRunner.class)
    // matchIfMissing 如果没有配置greeting.enabled属性
    @ConditionalOnProperty(name = "greeting.enabled", havingValue = "true", matchIfMissing = true)
    public GreetingApplicationRunner greetingApplicationRunner() {
        log.info("进行自动化配置：向上下文中注册Bean：GreetingApplicationRunner");
        //在以上条件都满足了之后，才会配置出来这个bean
        return new GreetingApplicationRunner();
    }
}
