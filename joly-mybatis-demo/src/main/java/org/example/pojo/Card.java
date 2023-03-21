package org.example.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author jrl
 * @date Create in 12:59 2023/3/21
 */
@Builder
@Data
public class Card {
	private Integer id;
	private String name;

	private List<Employee> employeeList;

	public Card() {
	}

	public Card(Integer id, String name, List<Employee> employeeList) {
		this.id = id;
		this.name = name;
		this.employeeList = employeeList;
	}
}
