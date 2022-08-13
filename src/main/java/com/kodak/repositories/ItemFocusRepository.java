package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemFocus;

@Repository
public interface ItemFocusRepository extends JpaRepository<ItemFocus, Long>{

}
