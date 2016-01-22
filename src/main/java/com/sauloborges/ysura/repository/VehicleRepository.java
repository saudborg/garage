package com.sauloborges.ysura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sauloborges.ysura.domain.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

	Vehicle findByLicencePlate(String licence);
	
	@Query
	Integer countByLicencePlate(String licence);

}
