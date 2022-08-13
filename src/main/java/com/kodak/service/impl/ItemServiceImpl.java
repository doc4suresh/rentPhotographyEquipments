package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Item;
import com.kodak.repositories.ItemRepository;
import com.kodak.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository repo;

	@Override
	public Item getById(long id) {
		return repo.getById(id);
	}

	@Override
	public List<Item> findAll() {
		return repo.findAll();
	}
	
	@Override
	public List<Item> findAllActive() {
		return repo.findAllActive();
	}

	@Override
	public Item save(Item item) {
		return repo.save(item);
	}

	@Override
	public Item update(Item item) {
		return repo.save(item);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
