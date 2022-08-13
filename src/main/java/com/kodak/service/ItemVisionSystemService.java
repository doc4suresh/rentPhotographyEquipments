package com.kodak.service;

import com.kodak.models.ItemVisionSystem;

public interface ItemVisionSystemService {
	ItemVisionSystem save(ItemVisionSystem entity);

	void deleteById(long id);
}
