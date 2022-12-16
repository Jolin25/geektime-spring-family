package geektime.spring.springbucks.waiter.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
// 根据配置文件里的prefix为order的配置项来生成bean的属性值
@ConfigurationProperties("order")
// 可以在程序运行时根据配置文件自动刷新这个bean
@RefreshScope
@Data
@Component
public class OrderProperties {
    private Integer discount = 100;
    private String waiterPrefix = "springbucks-";
}
