package com.kodak.service;

import com.kodak.models.ItemPerformance;

public interface ItemPerformanceService {
	ItemPerformance save(ItemPerformance entity);
	
	void deleteById(long id);
}
