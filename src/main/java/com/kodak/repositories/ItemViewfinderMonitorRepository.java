package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemViewfinderMonitor;

@Repository
public interface ItemViewfinderMonitorRepository extends JpaRepository<ItemViewfinderMonitor, Long>{

}
