package org.example;


import org.apache.ibatis.annotations.Param;
import org.example.mapper.EmployeesMapper;
import org.example.pojo.Employees;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SampleTest {
	@Autowired
	private EmployeesMapper employeesMapper;

	@Test
	public void selectFlexible() {
		List<Employees> employee = employeesMapper.selectFlexible("fist_name", "Mona");
		System.out.println(employee);
	}

	@Test
	public void selectById() {
		Employees employees = employeesMapper.selectById(1);
		System.out.println(employees);
	}

	@Test
	public void selectFirstNameById() {
		System.out.println(employeesMapper);
		String s = employeesMapper.selectFirstNameById(1);
		System.out.println(s);
	}
}