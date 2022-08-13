package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemFlash;
import com.kodak.repositories.ItemFlashRepository;
import com.kodak.service.ItemFlashService;

@Service
public class ItemFlashServiceImpl implements ItemFlashService {

	@Autowired
	private ItemFlashRepository repo;

	@Override
	public ItemFlash save(ItemFlash entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
