package com.kodak.service;

import com.kodak.models.ItemImaging;

public interface ItemImagingService {
	ItemImaging save(ItemImaging entity);
	
	void deleteById(long id);
}
