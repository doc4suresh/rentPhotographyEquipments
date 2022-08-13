package com.kodak.service;

import com.kodak.models.ItemPackagingInfo;

public interface ItemPackagingInfoService {
	ItemPackagingInfo save(ItemPackagingInfo entity);
	
	void deleteById(long id);
}
