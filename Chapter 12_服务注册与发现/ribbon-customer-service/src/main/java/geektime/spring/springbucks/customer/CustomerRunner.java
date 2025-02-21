package geektime.spring.springbucks.customer;

import geektime.spring.springbucks.customer.model.Coffee;
import geektime.spring.springbucks.customer.model.CoffeeOrder;
import geektime.spring.springbucks.customer.model.NewOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CustomerRunner implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        showServiceInstances();
        readMenu();
        Long id = orderCoffee();
        queryOrder(id);
    }

    /*说明其他服务（即使没有注册到注册中心组）
    可以找到（通过DiscoveryClient：Spring Cloud提供的抽象）注册到注册中心中的服务*/
    private void showServiceInstances() {
        log.info("DiscoveryClient: {}", discoveryClient.getClass().getName());
        discoveryClient.getInstances("waiter-service").forEach(s -> {
            log.info("Host: {}, Port: {}", s.getHost(), s.getPort());
        });
    }

    private void readMenu() {
        ParameterizedTypeReference<List<Coffee>> ptr =
                new ParameterizedTypeReference<List<Coffee>>() {
                };
        // 访问的时候根据注册到注册中心的名称来进行访问
        //waiter-service会在实际调用时被替换为具体的目标服务器和端口的，并不是真的去调用waiter-service这个地址，会做个转换
        ResponseEntity<List<Coffee>> list = restTemplate
                .exchange("http://waiter-service/coffee/", HttpMethod.GET, null, ptr);
        list.getBody().forEach(c -> log.info("Coffee: {}", c));
    }

    private Long orderCoffee() {
        NewOrderRequest orderRequest = NewOrderRequest.builder()
                .customer("Li Lei")
                .items(Arrays.asList("capuccino"))
                .build();
        RequestEntity<NewOrderRequest> request = RequestEntity
                .post(UriComponentsBuilder.fromUriString("http://waiter-service/order/").build().toUri())
                .body(orderRequest);
        ResponseEntity<CoffeeOrder> response = restTemplate.exchange(request, CoffeeOrder.class);
        log.info("Order Request Status Code: {}", response.getStatusCode());
        Long id = response.getBody().getId();
        log.info("Order ID: {}", id);
        return id;
    }

    private void queryOrder(Long id) {
        CoffeeOrder order = restTemplate
                .getForObject("http://waiter-service/order/{id}", CoffeeOrder.class, id);
        log.info("Order: {}", order);
    }
}
