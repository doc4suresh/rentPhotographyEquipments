package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemVisionSystem;

@Repository
public interface ItemVisionSystemRepository extends JpaRepository<ItemVisionSystem, Long>{

}
