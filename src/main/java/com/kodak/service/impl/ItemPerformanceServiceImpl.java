package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemPerformance;
import com.kodak.repositories.ItemPerformanceRepository;
import com.kodak.service.ItemPerformanceService;

@Service
public class ItemPerformanceServiceImpl implements ItemPerformanceService {

	@Autowired
	private ItemPerformanceRepository repo;

	@Override
	public ItemPerformance save(ItemPerformance entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
