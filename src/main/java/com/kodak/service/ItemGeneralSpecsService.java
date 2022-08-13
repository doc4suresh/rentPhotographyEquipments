package com.kodak.service;

import com.kodak.models.ItemGeneralSpecs;

public interface ItemGeneralSpecsService {

	ItemGeneralSpecs save(ItemGeneralSpecs entity);
	
	void deleteById(long id);
}
