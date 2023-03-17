package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 根据 MyBatis-Plus 官网介绍，做一个简单的小demo
 *  在 test 中进行了功能测试
 * @author jrl
 * @link {https://baomidou.com/pages/226c21/#%E5%88%9D%E5%A7%8B%E5%8C%96%E5%B7%A5%E7%A8%8B}
 */
// knowledge use:MyBatis Plus 也是需要把要扫描的 mapper 包注明的
@MapperScan("org.example.mapper")
@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}