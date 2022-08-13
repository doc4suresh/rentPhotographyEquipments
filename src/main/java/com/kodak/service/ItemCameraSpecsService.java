package com.kodak.service;

import com.kodak.models.ItemCameraSpecs;

public interface ItemCameraSpecsService {

	ItemCameraSpecs save(ItemCameraSpecs entity);

	void deleteById(long id);
}
