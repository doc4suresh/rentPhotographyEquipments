package com.kodak.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String address;
	private String designation;
	private String branch;
	private String username;
	private String password;
}
