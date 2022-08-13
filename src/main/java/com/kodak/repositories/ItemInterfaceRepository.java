package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemInterface;

@Repository
public interface ItemInterfaceRepository extends JpaRepository<ItemInterface, Long> {

}
