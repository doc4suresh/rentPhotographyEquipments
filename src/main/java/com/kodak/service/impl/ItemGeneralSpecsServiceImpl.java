package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemGeneralSpecs;
import com.kodak.repositories.ItemGeneralSpecsRepository;
import com.kodak.service.ItemGeneralSpecsService;

@Service
public class ItemGeneralSpecsServiceImpl implements ItemGeneralSpecsService {

	@Autowired
	private ItemGeneralSpecsRepository repo;

	@Override
	public ItemGeneralSpecs save(ItemGeneralSpecs entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
