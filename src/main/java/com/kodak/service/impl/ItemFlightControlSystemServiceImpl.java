package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemFlightControlSystem;
import com.kodak.repositories.ItemFlightControlSystemRepository;
import com.kodak.service.ItemFlightControlSystemService;

@Service
public class ItemFlightControlSystemServiceImpl implements ItemFlightControlSystemService {

	@Autowired
	private ItemFlightControlSystemRepository repo;

	@Override
	public ItemFlightControlSystem save(ItemFlightControlSystem entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
