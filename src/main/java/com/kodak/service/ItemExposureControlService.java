package com.kodak.service;

import com.kodak.models.ItemExposureControl;

public interface ItemExposureControlService {

	ItemExposureControl save(ItemExposureControl entity);
	
	void deleteById(long id);
}
