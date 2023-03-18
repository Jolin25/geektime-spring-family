package org.example;


import org.example.mapper.EmployeeMapper;
import org.example.pojo.Employee;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SampleTest {
	@Autowired
	private EmployeeMapper employeeMapper;

	@Test
	public void selectFlexible() {
		List<Employee> employee = employeeMapper.selectFlexibleGood("fist_name", "Mona");
		System.out.println(employee);
	}

	@Test
	public void selectById() {
		Employee employee = employeeMapper.selectById(1);
		System.out.println(employee);
	}

	@Test
	public void selectFirstNameById() {
		System.out.println(employeeMapper);
		String s = employeeMapper.selectFirstNameById(1);
		System.out.println(s);
	}
}