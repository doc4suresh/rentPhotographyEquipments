package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemPackagingInfo;
import com.kodak.repositories.ItemPackagingInfoRepository;
import com.kodak.service.ItemPackagingInfoService;

@Service
public class ItemPackagingInfoServiceImpl implements ItemPackagingInfoService {

	@Autowired
	private ItemPackagingInfoRepository repo;

	@Override
	public ItemPackagingInfo save(ItemPackagingInfo entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
