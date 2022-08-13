package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemFlightControlSystem;

@Repository
public interface ItemFlightControlSystemRepository extends JpaRepository<ItemFlightControlSystem, Long> {

}
