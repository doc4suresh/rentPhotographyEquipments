package com.kodak.service;

import com.kodak.models.ItemFlightControlSystem;

public interface ItemFlightControlSystemService {

	ItemFlightControlSystem save(ItemFlightControlSystem entity);

	void deleteById(long id);
}
