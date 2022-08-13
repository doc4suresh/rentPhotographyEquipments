package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemImaging;

@Repository
public interface ItemImagingRepository extends JpaRepository<ItemImaging, Long> {

}
