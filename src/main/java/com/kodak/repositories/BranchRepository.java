package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kodak.models.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query(value = "select * from branch e where name =:name", nativeQuery = true)
	Branch getByName(@Param("name") String name);
}
