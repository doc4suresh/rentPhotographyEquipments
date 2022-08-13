package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.Item3dObjectDetection;

@Repository
public interface Item3dObjectDetectionRepository extends JpaRepository<Item3dObjectDetection, Long> {

}
