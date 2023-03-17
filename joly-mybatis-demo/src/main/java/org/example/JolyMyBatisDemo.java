package org.example;

import org.example.mapper.EmployeesMapper;
import org.example.pojo.Employees;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
// knowledge use: 使用 MyBatis 必需说明 哪个包下面是 MyBatis 的
@MapperScan("org.example.mapper")
public class JolyMyBatisDemo implements ApplicationRunner {
	@Autowired
	EmployeesMapper employeesMapper;

	public static void main(String[] args) {
		SpringApplication.run(JolyMyBatisDemo.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// byAnnotation();
		// byXml();
		// selectFirstNameById();
		selectById();
		selectFlexible();
	}

	private void byXml() {
		int i = employeesMapper.insertXmlOne();
		System.out.println(i);
	}

	private void byAnnotation() {
		// Employees employee = new Employees(3,"Joly","-a","sichuan");
		// int insert = employeesMapper.insert(employee);
		// System.out.println(insert);
	}

	public void selectFlexible() {
		List<Employees> list = employeesMapper.selectFlexible("first_name", "Mona");
		System.out.println("selectFlexible:" + list);
		list.forEach(employee -> {
			System.out.println(employee.toString());
		});
	}

	public void selectById() {
		Employees employees = employeesMapper.selectById(1);
		System.out.println("selectById:" + employees);
	}

	public void selectFirstNameById() {
		System.out.println(employeesMapper);
		String s = employeesMapper.selectFirstNameById(1);
		System.out.println("selectFirstNameById:" + s);
	}
}