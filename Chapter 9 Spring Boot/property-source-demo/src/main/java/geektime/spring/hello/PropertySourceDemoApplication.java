package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 演示PropertySource，将配置文件里配置的值绑定到对应的属性上
 *
 * @author jrl
 * @date 2022/3/23
 */
@SpringBootApplication
@Slf4j
public class PropertySourceDemoApplication implements ApplicationRunner {
    @Value("${geektime.greeting}")
    private String greeting;

    public static void main(String[] args) {
        SpringApplication.run(PropertySourceDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("{}", greeting);
    }
}
