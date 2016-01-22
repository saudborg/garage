package com.sauloborges.ysura.service;

import java.util.List;

import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.SameLicencePlateException;

public interface VehicleService {
	
	public Vehicle save(Vehicle vehicle) throws SameLicencePlateException;
	
	public List<Vehicle> getAll();

	public Vehicle findByLicencePlate(String licence);
	
	public boolean isLicencePlateValid(String licence);

	public Vehicle findById(Integer id);

}
