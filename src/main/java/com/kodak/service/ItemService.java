package com.kodak.service;

import java.util.List;

import com.kodak.models.Item;

public interface ItemService {
	
	Item getById(long id);

	List<Item> findAll();
	
	List<Item> findAllActive();
	
	Item save(Item item);
	
	Item update(Item item);
	
	void deleteById(long id);
}
