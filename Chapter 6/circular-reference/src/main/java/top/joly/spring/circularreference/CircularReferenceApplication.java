package top.joly.spring.circularreference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.joly.spring.circularreference.spring.Spring_A;
import top.joly.spring.circularreference.spring.Spring_B;
import top.joly.spring.circularreference.spring.inter.Spring_A_Inter;
import top.joly.spring.circularreference.spring.inter.Spring_B_Inter;

@SpringBootApplication
public class CircularReferenceApplication implements ApplicationRunner {
    @Autowired
    Spring_A_Inter spring_a;
    @Autowired
    Spring_B_Inter spring_b;

    public static void main(String[] args) {
        SpringApplication.run(CircularReferenceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(spring_a);
        System.out.println(spring_b);
    }
}
