package com.kodak.service;

import java.util.List;
import java.util.Set;

import com.kodak.models.Employee;
import com.kodak.models.EmployeeRole;

public interface EmployeeService {

	List<Employee> findAll();
	
	List<Employee> findAllActive();
	
	Employee getById(long id);

	Employee findByUsername(String username);

	Employee createUser(Employee user, Set<EmployeeRole> empRoles) throws Exception;

	Employee updateEmployee(Employee user);

	void deleteById(long id);

	List<Employee> getAllManagers();
}
