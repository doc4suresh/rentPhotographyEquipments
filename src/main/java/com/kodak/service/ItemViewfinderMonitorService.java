package com.kodak.service;

import com.kodak.models.ItemViewfinderMonitor;

public interface ItemViewfinderMonitorService {
	ItemViewfinderMonitor save(ItemViewfinderMonitor entity);
	
	void deleteById(long id);
}
