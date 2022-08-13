package com.kodak.service;

import com.kodak.models.ItemFlash;

public interface ItemFlashService {

	ItemFlash save(ItemFlash entity);

	void deleteById(long id);
}
