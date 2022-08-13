package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemExposureControl;
import com.kodak.repositories.ItemExposureControlRepository;
import com.kodak.service.ItemExposureControlService;

@Service
public class ItemExposureControlServiceImpl implements ItemExposureControlService {

	@Autowired
	private ItemExposureControlRepository repo;

	@Override
	public ItemExposureControl save(ItemExposureControl entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
