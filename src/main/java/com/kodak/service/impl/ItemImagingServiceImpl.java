package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemImaging;
import com.kodak.repositories.ItemImagingRepository;
import com.kodak.service.ItemImagingService;

@Service
public class ItemImagingServiceImpl implements ItemImagingService {

	@Autowired
	private ItemImagingRepository repo;

	@Override
	public ItemImaging save(ItemImaging entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
