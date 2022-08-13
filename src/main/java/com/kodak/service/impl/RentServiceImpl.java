package com.kodak.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.constants.RentStatus;
import com.kodak.models.Customer;
import com.kodak.models.Employee;
import com.kodak.models.Rent;
import com.kodak.models.RentCartItem;
import com.kodak.repositories.RentRepository;
import com.kodak.service.RentService;

@Service
public class RentServiceImpl implements RentService {

	@Autowired
	private RentRepository repo;

	@Override
	public List<Rent> findAll() {
		return repo.findAll();
	}

	@Override
	public Rent getById(long id) {
		return repo.getById(id);
	}

	@Override
	public Rent intiateRentJob(Customer cus, Employee emp, List<RentCartItem> rentCartItemList, LocalDate startDate,
			LocalDate endDate, BigDecimal grandRentTotal, BigDecimal grandDepositTotal) {

		Rent rent = new Rent();
		rent.setStartDate(startDate);
		rent.setEndDate(endDate);
		rent.setGrandRentTotal(grandRentTotal);
		rent.setGrandDepositTotal(grandDepositTotal);
		rent.setGrandTotal(grandRentTotal.add(grandDepositTotal));
		rent.setStatus(RentStatus.INITIAL.toString());

		rent.setCustomer(cus);
		rent.setEmployee(emp);
		rent.setRentCartItemList(rentCartItemList);

		return repo.save(rent);
	}

	@Override
	public Rent findInitialItemForEmployee(Employee emp) {
		Rent rent = repo.findByStatusAndEmployee(RentStatus.INITIAL.toString(), emp);

		if (rent != null) {
			List<RentCartItem> revisedList = new ArrayList<>();
			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				if (!revisedList.contains(cartItem)) {
					revisedList.add(cartItem);
				}
			}
			rent.setRentCartItemList(revisedList);
		}

		return rent;
	}

	@Override
	public List<Rent> findOnGoingRentJobsByCustomer(Customer cus) {
		List<Rent> rentList = repo.findByStatusAndCustomer(RentStatus.RENTED.toString(), cus);

		for (Rent rent : rentList) {
			List<RentCartItem> revisedList = new ArrayList<>();
			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				if (!revisedList.contains(cartItem)) {
					revisedList.add(cartItem);
				}
			}
			rent.setRentCartItemList(revisedList);
		}

		return rentList;
	}

	@Override
	public Rent update(Rent rent) {
		return repo.save(rent);
	}

	@Override
	public void delete(Rent rent) {
		repo.delete(rent);
	}

	@Override
	public List<Rent> findByStatus(String status) {
		List<Rent> rentList = repo.findByStatus(status);

		for (Rent rent : rentList) {
			List<RentCartItem> revisedList = new ArrayList<>();
			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				if (!revisedList.contains(cartItem)) {
					revisedList.add(cartItem);
				}
			}
			rent.setRentCartItemList(revisedList);
		}

		return rentList;
	}

}
