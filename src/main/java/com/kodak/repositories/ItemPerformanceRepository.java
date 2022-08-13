package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemPerformance;

@Repository
public interface ItemPerformanceRepository extends JpaRepository<ItemPerformance, Long> {

}
