package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kodak.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query(value = "select * from category c where active=true", nativeQuery = true)
	List<Category> findAllActive();

	@Query(value = "select * from category c where catname =:name", nativeQuery = true)
	Category getByCatName(@Param("name") String name);
}
