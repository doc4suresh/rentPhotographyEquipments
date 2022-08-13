package com.kodak.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Employee;
import com.kodak.models.EmployeeRole;
import com.kodak.repositories.EmployeeRepository;
import com.kodak.repositories.RoleRepository;
import com.kodak.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public List<Employee> findAll() {
		List<Employee> list = empRepo.findAll();
		list.removeIf(b -> b.getUsername() != null && b.getUsername().equals("admin"));
		return list;
	}
	
	@Override
	public List<Employee> findAllActive() {
		List<Employee> list = empRepo.findAllActive();
		list.removeIf(b -> b.getUsername() != null && b.getUsername().equals("admin"));
		return list;
	}
	

	@Override
	public Employee getById(long id) {
		return empRepo.getById(id);
	}

	@Override
	public Employee findByUsername(String username) {
		return empRepo.findByUsername(username);
	}

	@Override
	public Employee createUser(Employee emp, Set<EmployeeRole> empRoles) throws Exception {
		Employee localEmp = empRepo.findByUsername(emp.getUsername());

		if (localEmp != null) {
			LOG.info("User {} already exists. Nothing will be done.", emp.getUsername());
		} else {
			for (EmployeeRole em : empRoles) {
				roleRepo.save(em.getRole());
			}

			emp.getEmployeeRoles().addAll(empRoles);

			localEmp = empRepo.save(emp);
		}

		return localEmp;
	}

	@Override
	public Employee updateEmployee(Employee user) {
		return empRepo.save(user);
	}

	@Override
	public void deleteById(long id) {
		empRepo.deleteById(id);
	}

	@Override
	public List<Employee> getAllManagers() {
		List<Employee> result = new ArrayList<>();

		List<Employee> empList = findAll();
		empList.forEach(emp -> {
			if (emp.getDesignation().equals("Manager"))
				result.add(emp);
		});

		return result;
	}

}
