package com.kodak.service;

import java.util.List;

import com.kodak.models.Customer;

public interface CustomerService {
	List<Customer> findAll();

	List<Customer> findAllActive();

	Customer save(Customer customer);

	Customer update(Customer customer);

	void deleteById(long id);

	Customer getById(long id);

	Customer findByFirstNameAndLastName(String firstName, String LastName);

	Customer findByMobile(String mobile);

	Customer findByMobile2(String mobile2);

	Customer findByNic(String nic);
}
