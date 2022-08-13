package com.kodak.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kodak.models.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

	@Query(value = "select * from brand b where active=true", nativeQuery = true)
	List<Brand> findAllActive();
	
	@Query(value = "select * from brand b where brandname =:name", nativeQuery = true)
	Brand getByBrandName(@Param("name") String name);
}
