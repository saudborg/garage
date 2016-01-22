package com.sauloborges.ysura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sauloborges.ysura.domain.ParkingSpace;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {

	@Query("SELECT s FROM ParkingSpace s where s.vehicle is null ")
	public List<ParkingSpace> getAllFreeSpaces();
	
	@Query("SELECT s FROM ParkingSpace s where s.vehicle.licencePlate = :licencePlate")
	public ParkingSpace findVehicleInAParkingSpaceByLicencePlate(@Param("licencePlate") String licencePlate);

}
