package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kodak.models.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query(value = "select * from item c where active=true", nativeQuery = true)
	List<Item> findAllActive();
}
