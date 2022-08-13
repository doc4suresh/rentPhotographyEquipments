package com.kodak.service;

import com.kodak.models.ItemInterface;

public interface ItemInterfaceService {
	ItemInterface save(ItemInterface entity);
	
	void deleteById(long id);
}
