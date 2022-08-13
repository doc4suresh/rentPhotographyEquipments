package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemExposureControl;

@Repository
public interface ItemExposureControlRepository extends JpaRepository<ItemExposureControl, Long> {

}
