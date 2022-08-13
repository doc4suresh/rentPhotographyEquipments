package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemVideoSpecs;
import com.kodak.repositories.ItemVideoSpecsRepository;
import com.kodak.service.ItemVideoSpecsService;

@Service
public class ItemVideoSpecsServiceImpl implements ItemVideoSpecsService {

	@Autowired
	private ItemVideoSpecsRepository repo;

	@Override
	public ItemVideoSpecs save(ItemVideoSpecs entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	};

}
