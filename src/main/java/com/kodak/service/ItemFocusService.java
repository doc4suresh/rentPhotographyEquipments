package com.kodak.service;

import com.kodak.models.ItemFocus;

public interface ItemFocusService {

	ItemFocus save(ItemFocus entity);
	
	void deleteById(long id);

}
