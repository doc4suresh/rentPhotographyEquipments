package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.ItemRemoteControllerOrTransmitter;
import com.kodak.repositories.ItemRemoteControllerOrTransmitterRepository;
import com.kodak.service.ItemRemoteControllerOrTransmitterService;

@Service
public class ItemRemoteControllerOrTransmitterServiceImpl implements ItemRemoteControllerOrTransmitterService {

	@Autowired
	private ItemRemoteControllerOrTransmitterRepository repo;

	@Override
	public ItemRemoteControllerOrTransmitter save(ItemRemoteControllerOrTransmitter entity) {
		return repo.save(entity);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

}
