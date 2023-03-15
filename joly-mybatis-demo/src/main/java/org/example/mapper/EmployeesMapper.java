package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.Employees;

/**
 * @author jrl
 * @date Create in 12:56 2023/3/15
 */
// knowledge use:应该是给 MyBatis 做拦截用的
@Mapper
public interface EmployeesMapper {
	/** knowledge point:
	 *  MyBatis 使用 JDBC 将 pojo 映射为 数据库字段*/
	@Insert("INSERT INTO employees VALUES(#{id},#{firstName},#{lastName},#{emailAddress})")
	int insert(Employees employee);

	int insertXmlOne();
}
