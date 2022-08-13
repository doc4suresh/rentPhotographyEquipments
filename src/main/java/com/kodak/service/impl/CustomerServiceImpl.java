package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Customer;
import com.kodak.repositories.CustomerRepository;
import com.kodak.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository repo;

	@Override
	public List<Customer> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Customer> findAllActive() {
		return repo.findAllActive();
	}

	@Override
	public Customer save(Customer customer) {
		return repo.save(customer);
	}

	@Override
	public Customer update(Customer customer) {
		return repo.save(customer);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

	@Override
	public Customer getById(long id) {
		return repo.getById(id);
	}

	@Override
	public Customer findByFirstNameAndLastName(String firstName, String LastName) {
		return repo.findByFirstNameAndLastName(firstName, LastName);
	}

	@Override
	public Customer findByMobile(String mobile) {
		return repo.findByMobile(mobile);
	}

	@Override
	public Customer findByMobile2(String mobile2) {
		return repo.findByMobile2(mobile2);
	}

	@Override
	public Customer findByNic(String nic) {
		return repo.findByNic(nic);
	}

}
