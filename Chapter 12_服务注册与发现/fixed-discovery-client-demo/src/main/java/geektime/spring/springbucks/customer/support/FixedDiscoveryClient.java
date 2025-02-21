package geektime.spring.springbucks.customer.support;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/** knowledge point:  Spring Boot 提供的，配置文件里设置bean的值 */
@ConfigurationProperties("waiter")
@Setter
public class FixedDiscoveryClient implements DiscoveryClient {
    public static final String SERVICE_ID = "waiter-service";
    // waiter.services
    private List<String> services;

    @Override
    public String description() {
        return "DiscoveryClient that uses service.list from application.yml.";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        /** knowledge point:  实际情况肯定是客户端要去服务端进行查询的*/
        if (!SERVICE_ID.equalsIgnoreCase(serviceId)) {
            return Collections.emptyList();
        }
        // 这里忽略了很多边界条件判断，认为就是 HOST:PORT 形式
        return services.stream()
                .map(s -> new DefaultServiceInstance(s,
                        SERVICE_ID,
                        s.split(":")[0],
                        Integer.parseInt(s.split(":")[1]),
                        false))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getServices() {
        /** knowledge point:  实际情况肯定是客户端要去服务端进行查询的*/
        return Collections.singletonList(SERVICE_ID);
    }
}
