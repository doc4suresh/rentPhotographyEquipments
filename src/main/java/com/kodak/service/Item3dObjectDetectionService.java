package com.kodak.service;

import com.kodak.models.Item3dObjectDetection;

public interface Item3dObjectDetectionService {

	Item3dObjectDetection save(Item3dObjectDetection entity);
	
	void deleteById(long id);
}
