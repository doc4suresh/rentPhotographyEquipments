package com.kodak.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.kodak.models.Customer;
import com.kodak.models.Employee;
import com.kodak.models.Rent;
import com.kodak.models.RentCartItem;

public interface RentService {

	List<Rent> findAll();

	Rent getById(long id);

	Rent intiateRentJob(Customer cus, Employee emp, List<RentCartItem> rentCartItemList, LocalDate startDate,
			LocalDate endDate, BigDecimal grandRentTotal, BigDecimal grandDepositTotal);

	Rent findInitialItemForEmployee(Employee emp);

	List<Rent> findOnGoingRentJobsByCustomer(Customer cus);

	Rent update(Rent rent);

	void delete(Rent rent);

	List<Rent> findByStatus(String status);
}
