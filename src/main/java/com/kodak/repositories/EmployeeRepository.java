package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kodak.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = "select * from employee e where enabled=true", nativeQuery = true)
	List<Employee> findAllActive();

	@Query(value = "select * from employee e where username =:username", nativeQuery = true)
	Employee findByUsername(@Param("username") String username);
}
