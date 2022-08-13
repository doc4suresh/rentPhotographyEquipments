package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemBattery;

@Repository
public interface ItemBatteryRepository extends JpaRepository<ItemBattery, Long> {

}
