package geektime.spring.web.context;

import geektime.spring.web.foo.FooConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 1.容器：上下文
 * spring里可以存在多个容器，容器之间可以有父子关系。类似类加载器的双亲机制。
 * 2.aop与容器：
 * 2.1 是配套的，aop只能增强对应的容器里的bean
 * 2.2 如果想要增加父子容器，那么可以在父子容器中都开启aop增强，然后把切面定义在父容器之中。（子容器应该就会自动去寻找父容器的aop增强，来实现aop增强）
 *
 * @author Joly
 */
@SpringBootApplication
@Slf4j
public class ContextHierarchyDemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ContextHierarchyDemoApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 加载配置类：通过spring
        ApplicationContext fooContext = new AnnotationConfigApplicationContext(FooConfig.class);
        //加载配置文件：通过spring，并且声明了该配置文件的父级ApplicationContext
        // DONE_Joly:如何说明父子关系，如何使用父子关系，这个父子关系实际代表了什么  --->
        // 如下就可以说明父子关系
        // 使用：在取bean的时候，会自动根据父子关系来找bean，当前容器里没有对应的bean，就会去父容器里面找
        // 父子关系：代表了B子容器可以从A父容器中取bean
        ClassPathXmlApplicationContext barContext = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext.xml"}, fooContext);
        // Done_Joly:这里的取bean约定是什么--->根据bean id 来取
        TestBean bean = fooContext.getBean("testBeanX", TestBean.class);
        bean.hello(); // foo // after hello

        log.info("=============");

        bean = barContext.getBean("testBeanX", TestBean.class);
        bean.hello(); // bar // after hello （要看xml里面有没有开启aop）

        bean = barContext.getBean("testBeanY", TestBean.class);
        bean.hello(); // foo // after hello
    }
}
