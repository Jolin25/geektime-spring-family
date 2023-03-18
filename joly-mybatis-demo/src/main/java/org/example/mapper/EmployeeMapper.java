package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.Employee;

import java.util.List;

/**
 * @author jrl
 * @date Create in 12:56 2023/3/15
 */
// knowledge use:应该是给 MyBatis 做拦截用的
@Mapper
public interface EmployeeMapper {
	/**
	 * knowledge point:
	 * MyBatis 使用 JDBC 将 pojo 映射为 数据库字段
	 */
	@Insert("INSERT INTO employees VALUES(#{id},#{firstName},#{lastName},#{emailAddress})")
	int insert(Employee employee);

	int insertXmlOne();

	List<Employee>  selectFlexibleGood(@Param("column") String column, @Param("value") String value);
	List<Employee>  selectFlexibleBad(@Param("column") String column, @Param("value") String value);

	Employee selectById(int id);

	String selectFirstNameById(int id);

	void insertOne();

	int insertOneByType(Employee employee);

	int insertOneByTypeNoId(Employee employee);

	List<Employee> selectUseIfOne(Employee employee);

	List<Employee> selectUseChooseOne(Employee employee);

	void updateUseSet(Employee employee);

	void updateUseTrim(Employee employee);

	List<Employee> selectUseForeach(List<Integer> list);

	List<Employee> selectUseBind(String value);

	Employee selectOverload(@Param("firstName") String firstName);
	Employee selectOverload();
}
