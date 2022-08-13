package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemFlash;

@Repository
public interface ItemFlashRepository extends JpaRepository<ItemFlash, Long> {

}
