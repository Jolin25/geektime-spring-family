package org.example;

import org.example.mapper.UserMapper;
import org.example.pojo.SysUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SampleTest {
	@Autowired
	private UserMapper userMapper;

	// @Test
	// public void testSelect() {
	// 	System.out.println(("----- selectAll method test ------"));
	// 	List<SysUser> userList = userMapper.selectList(null);
	// 	Assertions.assertEquals(5, userList.size());
	// 	userList.forEach(System.out::println);
	// }

	@Test
	public void testSelectByXML1() {
		System.out.println("------------testSelectByXML1------------");
		List<SysUser> list = userMapper.selectByXML1();
		list.forEach(System.out::println);
	}
}