package org.example.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author jrl
 * @date Create in 12:52 2023/3/15
 */
@Builder
@Data
public class Employee {
	int id;
	String firstName;
	String lastName;
	String emailAddress;

	public Employee() {
	}

	public Employee(int id, String firstName, String lastName, String emailAddress) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				'}';
	}
}
