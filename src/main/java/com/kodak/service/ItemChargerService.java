package com.kodak.service;

import com.kodak.models.ItemCharger;

public interface ItemChargerService {

	ItemCharger save(ItemCharger entity);
	
	void deleteById(long id);
}
