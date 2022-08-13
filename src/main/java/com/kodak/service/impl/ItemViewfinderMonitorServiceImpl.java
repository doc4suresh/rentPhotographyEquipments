package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemViewfinderMonitor;
import com.kodak.repositories.ItemViewfinderMonitorRepository;
import com.kodak.service.ItemViewfinderMonitorService;

@Service
public class ItemViewfinderMonitorServiceImpl implements ItemViewfinderMonitorService {

	@Autowired
	private ItemViewfinderMonitorRepository repo;

	public ItemViewfinderMonitor save(ItemViewfinderMonitor entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}
}
