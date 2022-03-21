package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@SpringBootApplication
@Slf4j
public class CustomerServiceApplication implements ApplicationRunner {


    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        // DONE_Joly: 这一串操作在干嘛
        /** knowledge point:
         *  因为这个项目是在消费REST服务，类似于浏览器，是不需要tomcat的，
         *  所以就做个性化配置。
         *  否则，因为引入了web依赖，Spring Boot就会自动的启动一个tomcat
         * */
        new SpringApplicationBuilder()
                .sources(CustomerServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    /**
     * knowledge point:
     * 因为Spring Boot 没有自动配置 Rest Template，所以就要我们自己注入
     */
    @Bean
    @Scope("SINGLETON") // spring默认的bean的作用域就是singleton
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return new RestTemplate();  // 第一种方式：直接new
        return builder.build(); // 第二种方式： Spring Boot 提供了 RestTemplateBuilder，来构造RestTemplate
    }

    @Override
    public void run(ApplicationArguments args) {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/coffee/{id}")
                // 给路径参数传参
                .build(1);
        ResponseEntity<Coffee> c = restTemplate.getForEntity(uri, Coffee.class);
        log.info("Response Status: {}, Response Headers: {}", c.getStatusCode(), c.getHeaders().toString());
        log.info("Coffee: {}", c.getBody());

        String coffeeUri = "http://localhost:8080/coffee/";
        Coffee request = Coffee.builder()
                .name("Americano")
                .price(BigDecimal.valueOf(25.00))
                .build();
        Coffee response = restTemplate.postForObject(coffeeUri, request, Coffee.class);
        log.info("New Coffee: {}", response);

        String s = restTemplate.getForObject(coffeeUri, String.class);
        log.info("String: {}", s);
    }
}
