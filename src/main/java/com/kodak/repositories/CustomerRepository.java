package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kodak.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query(value = "select * from customer c where active=true", nativeQuery = true)
	List<Customer> findAllActive();

	Customer findByFirstNameAndLastName(String firstName, String LastName);

	Customer findByMobile(String mobile);

	Customer findByMobile2(String mobile2);

	Customer findByNic(String nic);
}
