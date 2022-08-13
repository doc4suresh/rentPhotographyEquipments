package com.kodak.service;

import java.util.List;

import com.kodak.models.Item;
import com.kodak.models.RentCartItem;

public interface RentCartItemService {

	RentCartItem addItemsToCart(Item item, int qty, String username);

	void deleteById(long id);

	List<RentCartItem> findInprogressItemForUser(String username);

	RentCartItem update(RentCartItem cartItem);
}
