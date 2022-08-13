package com.kodak.service;

import com.kodak.models.ItemVideoSpecs;

public interface ItemVideoSpecsService {
	ItemVideoSpecs save(ItemVideoSpecs entity);
	
	void deleteById(long id);
}
