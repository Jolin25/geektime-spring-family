package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.support.CustomConnectionKeepAliveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
// 开启Spring Cloud 对 Hystrix的支持
@EnableCircuitBreaker
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.custom()
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .disableAutomaticRetries()
                .setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
                .build();
    }
}
