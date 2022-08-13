package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
