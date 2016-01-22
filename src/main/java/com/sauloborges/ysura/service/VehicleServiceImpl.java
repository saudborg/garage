package com.sauloborges.ysura.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.SameLicencePlateException;
import com.sauloborges.ysura.repository.VehicleRepository;

@Service
public class VehicleServiceImpl implements VehicleService {

	private final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

	@Autowired
	private VehicleRepository vehicleRepository;

	/**
	 * Save a vehicle
	 * 
	 * Throw a excpetion if exists one with the same licence plate
	 */
	@Override
	public Vehicle save(Vehicle vehicle) throws SameLicencePlateException {
		logger.debug("call vehicle.save params=[" + vehicle + "]");
		if (isLicencePlateValid(vehicle.getLicencePlate())) {
			vehicle = vehicleRepository.save(vehicle);
			logger.info("call vehicle.save - ok [id=" + vehicle.getId() + "]");
			return vehicle;
		} else {
			throw new SameLicencePlateException(vehicle.getLicencePlate());
		}
	}

	@Override
	public List<Vehicle> getAll() {
		logger.debug("call vehicle.getAll");
		return vehicleRepository.findAll();
	}

	/**
	 * Find a vehicle by licence Plate
	 */
	@Override
	public Vehicle findByLicencePlate(String licence) {
		logger.debug("call vehicle.findByLicencePlate params=[" + licence + "]");
		return vehicleRepository.findByLicencePlate(licence);
	}

	/**
	 * Count how many licences plate in the system
	 * 
	 */
	@Override
	public boolean isLicencePlateValid(String licence) {
		logger.debug("call vehicle.isLicencePlateValid params=[" + licence + "]");
		Integer count = vehicleRepository.countByLicencePlate(licence);
		logger.debug("call vehicle.isLicencePlateValid result=[" + count + "]");
		if (count > 0) {
			return false;
		} else
			return true;
	}

	@Override
	public Vehicle findById(Integer id) {
		return vehicleRepository.findOne(id);
	}

}
