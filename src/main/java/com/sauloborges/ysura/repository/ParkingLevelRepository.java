package com.sauloborges.ysura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sauloborges.ysura.domain.ParkingLevel;

@Repository
public interface ParkingLevelRepository extends JpaRepository<ParkingLevel, Integer>{

}
