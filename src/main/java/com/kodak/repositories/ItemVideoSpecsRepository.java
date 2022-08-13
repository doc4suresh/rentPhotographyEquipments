package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemVideoSpecs;

@Repository
public interface ItemVideoSpecsRepository extends JpaRepository<ItemVideoSpecs, Long> {

}
