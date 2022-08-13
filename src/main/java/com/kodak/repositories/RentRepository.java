package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodak.models.Customer;
import com.kodak.models.Employee;
import com.kodak.models.Rent;

public interface RentRepository extends JpaRepository<Rent, Long> {

	Rent findByStatusAndEmployee(String status, Employee employee);

	List<Rent> findByStatusAndCustomer(String status, Customer customer);

	List<Rent> findByStatus(String status);
}
