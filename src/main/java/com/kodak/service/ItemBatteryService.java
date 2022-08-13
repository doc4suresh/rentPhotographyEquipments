package com.kodak.service;

import com.kodak.models.ItemBattery;

public interface ItemBatteryService {

	ItemBattery save(ItemBattery entity);

	void deleteById(long id);
}
