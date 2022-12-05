package geektime.spring.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 演示添加配置文件（就是把自己的配置文件给注册上去）：
 * 1.实现EnvironmentPostProcessor，Spring会来运行这个接口的方法的
 *
 * @author jrl
 * @date 2022/3/23
 */
@Slf4j
public class YapfEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Resource resource = new ClassPathResource("yapf.properties");
        try {
            PropertySource ps = loader.load("YetAnotherPropertiesFile", resource)
                    .get(0);
            // 添加到第一的位置,添加的位置决定了配置的优先级
            // propertySources.addFirst(ps);
            propertySources.addLast(ps);
        } catch (Exception e) {
            log.error("Exception!", e);
        }
    }
}
