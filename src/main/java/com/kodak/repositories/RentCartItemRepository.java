package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodak.models.Employee;
import com.kodak.models.Item;
import com.kodak.models.RentCartItem;

public interface RentCartItemRepository extends JpaRepository<RentCartItem, Long> {

	List<RentCartItem> findByStatusAndEmployeeAndItem(String status, Employee employee, Item item);

	List<RentCartItem> findByStatusAndEmployee(String status, Employee employee);
}
