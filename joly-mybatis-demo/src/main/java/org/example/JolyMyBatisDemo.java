package org.example;

import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.session.SqlSession;
import org.example.mapper.CardMapper;
import org.example.mapper.EmployeeMapper;
import org.example.pojo.Card;
import org.example.pojo.Employee;
import org.example.pojo.Page;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
// knowledge use: 使用 MyBatis 必需说明 哪个包下面是 MyBatis 的
@MapperScan("org.example.mapper")
public class JolyMyBatisDemo implements ApplicationRunner {
	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	CardMapper cardMapper;

	@Autowired
	SqlSession sqlSession;

	public static void main(String[] args) {
		SpringApplication.run(JolyMyBatisDemo.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// basic();
		// select();
		// insert();
		// dynamicSQL();
		// overloadWrong();
		// overloadWrite();
		// 这个不算
		// encapsulation();
		// insertBatch();
		// selectMapping();
		batch();
	}

	private void batch() {
		System.out.println(sqlSession);

	}

	private void selectMapping() {
		Employee employee = employeeMapper.selectMapping(1012);
		System.out.println(employee);
		System.out.println("--------------");
		List<Card> cards = cardMapper.oneToMany();
		cards.forEach(System.out::println);
		System.out.println("---------------");
		List<Card> cards2 = cardMapper.oneToManyFather();
		cards2.forEach(System.out::println);
	}

	private void insertBatch() {
		List<Employee> list = new ArrayList<>();
		list.add(new Employee(0, "00", "batch", "home"));
		list.add(new Employee(0, "10", "batch", "home"));
		list.add(new Employee(0, "20", "batch", "home"));
		employeeMapper.insertBatch(list);
		list.forEach(System.out::println);
	}

	private void encapsulation() {
		Page<Employee> employeePage = employeeMapper.encapsulation();
		List<Employee> content = employeePage.getContent();
		content.forEach(System.out::println);
	}

	private void overloadWrite() {
		// knowledge use:
		// Mybatis 的 Dao 接口可以有多个重载方法，但是多个接口对应的映射必须只有一个，否则启动会报错。
		Employee employee = employeeMapper.selectOverload("fix");
		System.out.println(employee);
		Employee employee1 = employeeMapper.selectOverload();
		System.out.println(employee1);
	}

	private void overloadWrong() {
		//  Mapped Statements collection already contains value for
		//  org.example.mapper.EmployeeMapper.selectOverload
		//  在初始化的时候就报错了，无需等到执行
		// 这是因为 在 xml 文件中同一个 id 只允许出现一个
		// 此时，在 xml 中 “selectOverload”有两个
		Employee a = employeeMapper.selectOverload("a");
		Employee a2 = employeeMapper.selectOverload();
		System.out.println(a);
		System.out.println(a2);

	}

	private void dynamicSQL() {
		selectUseIfOne();
		selectUseChooseOne();
		updateUseSet();
		updateUseTrim();
		selectUseForeach();
		selectUseBind();
	}

	private void selectUseBind() {
		List<Employee> list = employeeMapper.selectUseBind("oly");
		System.out.println("-----selectUseBind-----");
		list.forEach(System.out::println);
	}

	private void selectUseForeach() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(200);
		list.add(300);
		List<Employee> list1 = employeeMapper.selectUseForeach(list);
		System.out.println("-----selectUseForeach-----");
		list1.forEach(System.out::println);
	}

	private void updateUseTrim() {
		Employee employee = Employee.builder()
				.lastName("updateTwo")
				.firstName("updateTwo")
				.emailAddress("AlibabaTwo")
				.id(1)
				.build();

		employeeMapper.updateUseSet(employee);
	}

	private void updateUseSet() {
		Employee employee = Employee.builder()
				.lastName("updateOne")
				.firstName("updateOne")
				.emailAddress("Alibaba")
				.id(1)
				.build();

		employeeMapper.updateUseSet(employee);
	}

	private void selectUseChooseOne() {
		Employee employee = Employee.builder()
				.lastName("typeNoSpecified")
				.build();

		List<Employee> list = employeeMapper.selectUseChooseOne(employee);
		list.forEach(System.out::println);

	}

	private void selectUseIfOne() {
		Employee employee = Employee.builder()
				.firstName("1")
				.build();

		List<Employee> list = employeeMapper.selectUseIfOne(employee);
		list.forEach(System.out::println);
		System.out.println(list.get(0).getFirstName());
	}

	private void insert() {
		// insertOne();
		insertOneByType();
		insertOneByTypeNoId();
	}

	private void insertOneByTypeNoId() {
		Employee employee = Employee.builder().firstName("1").lastName("typeNoSpecified").emailAddress("coffee").build();
		int i = employeeMapper.insertOneByTypeNoId(employee);
		System.out.println(i);
	}

	private void insertOneByType() {
		Employee employee = Employee.builder().id(300).firstName("1").lastName("type").emailAddress("coffee").build();
		int i = employeeMapper.insertOneByType(employee);
		System.out.println(i);
	}

	private void select() {
		selectFirstNameById();
		selectById();
		selectFlexible();
	}

	private void basic() {
		byAnnotation();
		byXml();
	}

	private void byXml() {
		int i = employeeMapper.insertXmlOne();
		System.out.println(i);
	}

	private void byAnnotation() {
		// Employees employee = new Employees(3,"Joly","-a","sichuan");
		// int insert = employeeMapper.insert(employee);
		// System.out.println(insert);
	}

	public void selectFlexible() {
		List<Employee> list = employeeMapper.selectFlexibleGood("first_name", "Mona");
		System.out.println("selectFlexible:" + list);
		list.forEach(employee -> {
			System.out.println(employee.toString());
		});


		List<Employee> listBad = employeeMapper.selectFlexibleBad("first_name", "Mona");
		System.out.println("selectFlexibleBadddddddddddd:" + listBad);
		listBad.forEach(employee -> {
			System.out.println(employee.toString());
		});
	}

	public void selectById() {
		Employee employee = employeeMapper.selectById(1);
		System.out.println("selectById:" + employee);
	}

	public void selectFirstNameById() {
		System.out.println(employeeMapper);
		String s = employeeMapper.selectFirstNameById(1);
		System.out.println("selectFirstNameById:" + s);
	}

	public void insertOne() {
		employeeMapper.insertOne();
	}
}