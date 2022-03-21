package geektime.spring.hello.greeting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 这个作为自动配置类
 * 这个自动配置类的作用是将Greeting实例放入容器中
* @author jrl
* @date 2022/3/21
*/
@Configuration
public class GreetingAutoConfiguration {
    @Bean
    public static GreetingBeanFactoryPostProcessor greetingBeanFactoryPostProcessor() {
        return new GreetingBeanFactoryPostProcessor();
    }
}
