package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemCameraSpecs;
import com.kodak.repositories.ItemCameraSpecsRepository;
import com.kodak.service.ItemCameraSpecsService;

@Service
public class ItemCameraSpecsServiceImpl implements ItemCameraSpecsService {

	@Autowired
	private ItemCameraSpecsRepository repo;

	@Override
	public ItemCameraSpecs save(ItemCameraSpecs entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
