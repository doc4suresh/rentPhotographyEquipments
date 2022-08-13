package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemFocus;
import com.kodak.repositories.ItemFocusRepository;
import com.kodak.service.ItemFocusService;

@Service
public class ItemFocusServiceImpl implements ItemFocusService {

	@Autowired
	private ItemFocusRepository repo;

	@Override
	public ItemFocus save(ItemFocus entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
