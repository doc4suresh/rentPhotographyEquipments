package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemVisionSystem;
import com.kodak.repositories.ItemVisionSystemRepository;
import com.kodak.service.ItemVisionSystemService;

@Service
public class ItemVisionSystemServiceImpl implements ItemVisionSystemService {

	@Autowired
	private ItemVisionSystemRepository repo;

	@Override
	public ItemVisionSystem save(ItemVisionSystem entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
