package com.kodak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodak.models.ItemPackagingInfo;

@Repository
public interface ItemPackagingInfoRepository extends JpaRepository<ItemPackagingInfo, Long>{

}
