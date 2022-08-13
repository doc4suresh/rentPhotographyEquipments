package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemInterface;
import com.kodak.repositories.ItemInterfaceRepository;
import com.kodak.service.ItemInterfaceService;

@Service
public class ItemInterfaceServiceImpl implements ItemInterfaceService {

	@Autowired
	private ItemInterfaceRepository repo;

	@Override
	public ItemInterface save(ItemInterface entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
