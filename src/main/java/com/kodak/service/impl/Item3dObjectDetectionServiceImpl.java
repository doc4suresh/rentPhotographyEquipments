package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Item3dObjectDetection;
import com.kodak.repositories.Item3dObjectDetectionRepository;
import com.kodak.service.Item3dObjectDetectionService;

@Service
public class Item3dObjectDetectionServiceImpl implements Item3dObjectDetectionService {

	@Autowired
	private Item3dObjectDetectionRepository repo;

	@Override
	public Item3dObjectDetection save(Item3dObjectDetection entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
