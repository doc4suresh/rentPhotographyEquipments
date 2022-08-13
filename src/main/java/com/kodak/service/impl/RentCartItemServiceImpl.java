package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.constants.RentItemStatus;
import com.kodak.models.Employee;
import com.kodak.models.Item;
import com.kodak.models.RentCartItem;
import com.kodak.repositories.RentCartItemRepository;
import com.kodak.service.EmployeeService;
import com.kodak.service.ItemService;
import com.kodak.service.RentCartItemService;

@Service
public class RentCartItemServiceImpl implements RentCartItemService {

	@Autowired
	private RentCartItemRepository repo;

	@Autowired
	private EmployeeService empService;

	@Autowired
	private ItemService itemService;

	@Override
	public RentCartItem addItemsToCart(Item item, int qty, String username) {
		Employee emp = empService.findByUsername(username);

		// If Item already added to cart, just update the qty only
		List<RentCartItem> inprogressItems = repo.findByStatusAndEmployeeAndItem(RentItemStatus.INPROGRESS.toString(),
				emp, item);

		// Is this item already selected, only update the qty and sub total of the cart
		// item.
		for (RentCartItem inprogressItem : inprogressItems) {
			// Reserve Item available qty for rent
			if (qty > inprogressItem.getQty()) {
				int newQty = qty - inprogressItem.getQty();
				item.setAvailable(item.getAvailable() - newQty);
			} else {
				int newQty = inprogressItem.getQty() - qty;
				item.setAvailable(item.getAvailable() + newQty);
			}
			itemService.update(item);

			inprogressItem.setQty(qty);
			return repo.save(inprogressItem);
		}

		// Reserve Item available qty for rent
		item.setAvailable(item.getAvailable() - qty);
		itemService.update(item);

		// add new Item to cart
		RentCartItem cartItem = new RentCartItem();
		cartItem.setItem(item);
		cartItem.setQty(qty);
		cartItem.setStatus(RentItemStatus.INPROGRESS.toString());
		cartItem.setEmployee(emp);

		return repo.save(cartItem);
	}

	@Override
	public RentCartItem update(RentCartItem cartItem) {
		return repo.save(cartItem);
	}

	@Override
	public List<RentCartItem> findInprogressItemForUser(String username) {
		Employee emp = empService.findByUsername(username);
		return repo.findByStatusAndEmployee(RentItemStatus.INPROGRESS.toString(), emp);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
