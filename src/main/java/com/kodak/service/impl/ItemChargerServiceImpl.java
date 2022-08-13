package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemCharger;
import com.kodak.repositories.ItemChargerRepository;
import com.kodak.service.ItemChargerService;

@Service
public class ItemChargerServiceImpl implements ItemChargerService {

	@Autowired
	private ItemChargerRepository repo;

	@Override
	public ItemCharger save(ItemCharger entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
