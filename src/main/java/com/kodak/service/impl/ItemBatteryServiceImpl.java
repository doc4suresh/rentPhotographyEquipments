package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemBattery;
import com.kodak.repositories.ItemBatteryRepository;
import com.kodak.service.ItemBatteryService;

@Service
public class ItemBatteryServiceImpl implements ItemBatteryService {

	@Autowired
	private ItemBatteryRepository repo;

	@Override
	public ItemBattery save(ItemBattery entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

	
}
