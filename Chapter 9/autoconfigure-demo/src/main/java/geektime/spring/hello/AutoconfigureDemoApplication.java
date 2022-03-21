package geektime.spring.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Spring Boot 会去扫描这个注解所在包之下的所有类的配置，所以一般就是整个项目了
@SpringBootApplication
public class AutoconfigureDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoconfigureDemoApplication.class, args);
	}

}
