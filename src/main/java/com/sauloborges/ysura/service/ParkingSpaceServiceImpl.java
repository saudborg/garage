package com.sauloborges.ysura.service;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;
import com.sauloborges.ysura.repository.ParkingSpaceRepository;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

	private final Logger logger = LoggerFactory.getLogger(ParkingSpaceServiceImpl.class);

	private static final Random RANDOM = new Random();

	@Autowired
	private ParkingSpaceRepository parkingSpaceRepository;

	/**
	 * Save a parking space
	 */
	@Override
	public ParkingSpace save(ParkingSpace parkingSpace) {
		logger.debug("call parkingSpace.save [" + parkingSpace + "]");
		parkingSpace = parkingSpaceRepository.save(parkingSpace);
		logger.debug("call parkingSpace.save saved[id=" + parkingSpace.getId() + "]");

		return parkingSpace;
	}

	@Override
	public List<ParkingSpace> getList() {
		logger.debug("call parkingSpace.findAll");
		return parkingSpaceRepository.findAll();
	}

	public ParkingSpace updateToOccupied(ParkingSpace parkingSpace, Vehicle vehicle) {
		logger.debug("call parkingSpace.updateToOcupped params[" + parkingSpace + ", " + vehicle + "]");
		parkingSpace.setVehicle(vehicle);
		parkingSpace.setTimeEnter(Calendar.getInstance());
		return this.save(parkingSpace);
	}

	public ParkingSpace updateToFree(ParkingSpace parkingSpace) {
		logger.debug("call parkingSpace.updateToFree params[" + parkingSpace + "]");

		// set null means the space is free
		parkingSpace.setVehicle(null);
		parkingSpace.setTimeEnter(null);
		return this.save(parkingSpace);
	}

	/**
	 * Return a list of spaces that are free
	 */
	@Override
	public List<ParkingSpace> getFreeSpaces() {
		logger.debug("call parkingSpace.findAllFreeSpaces");
		List<ParkingSpace> allFreeSpaces = parkingSpaceRepository.getAllFreeSpaces();
		logger.debug("call parkingSpace.findAllFreeSpaces - ok");

		return allFreeSpaces;
	}

	/**
	 * Get a free space in a garage. If is full throw a excpetion telling this
	 */
	@Override
	public ParkingSpace getAFreeSpace() throws NoMoreSpacesException {
		logger.debug("call parkingSpace.getAFreeSpace");
		List<ParkingSpace> allFreeSpaces = this.getFreeSpaces();
		if (allFreeSpaces.size() == 0) {
			throw new NoMoreSpacesException();
		}

		ParkingSpace space = allFreeSpaces.get(RANDOM.nextInt(allFreeSpaces.size()));
		logger.debug("call parkingSpace.getAFreeSpace space=[" + space.getParkingLevel().getNumber() + ","
				+ space.getNumber());
		return space;
	}

	@Override
	public ParkingSpace findVehicleInAParkingSpaceByLicencePlate(String licencePlate) {
		logger.debug("call parkingSpace.findVehicleInAParkingSpaceByLicencePlate param=[" + licencePlate + "]");
		return parkingSpaceRepository.findVehicleInAParkingSpaceByLicencePlate(licencePlate);
	}

	@Override
	public ParkingSpace findById(Integer id) {
		logger.debug("call parkingSpace.findById param=[" + id + "]");
		return parkingSpaceRepository.findOne(id);
	}

}
