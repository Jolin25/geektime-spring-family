package top.jolyne.spring0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.jolyne.spring0.scope.BeanA;

/**
 * 测试 单体应用中 scope 的 singleton 和 prototype
 *
 * @author Joly
 */
@SpringBootApplication
public class Spring0Application implements ApplicationRunner {
    @Autowired
    BeanA beanA;
    @Autowired
    BeanA beanB;

    public static void main(String[] args) {
        SpringApplication.run(Spring0Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(beanA == beanB);//scope 为 singleton时，为 true
        System.out.println(beanA == beanB);//scope 为 prototype时，为 false
    }
}
