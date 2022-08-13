package com.kodak.service;

import com.kodak.models.ItemRemoteControllerOrTransmitter;

public interface ItemRemoteControllerOrTransmitterService {
	ItemRemoteControllerOrTransmitter save(ItemRemoteControllerOrTransmitter entity);
	
	void deleteById(long id);
}
