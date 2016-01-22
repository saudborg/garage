package com.sauloborges.ysura.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sauloborges.ysura.domain.ParkingLevel;
import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.TypeVehiclesEnum;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;
import com.sauloborges.ysura.exception.SameLicencePlateException;
import com.sauloborges.ysura.repository.ParkingLevelRepository;
import com.sauloborges.ysura.util.LicencePlateGenerator;

@Service
public class ParkingLevelServiceImpl implements ParkingLevelService {

	private final Logger logger = LoggerFactory.getLogger(ParkingLevelServiceImpl.class);

	@Autowired
	private ParkingLevelRepository parkingLevelRepository;

	@Autowired
	private ParkingSpaceService parkingSpaceService;
	
	@Autowired
	private VehicleService vehicleService;

	/**
	 * Create and save a new level on building
	 */
	public ParkingLevel save(ParkingLevel level) {
		logger.debug("call parkingLevel.save [" + level + "]");
		level = parkingLevelRepository.save(level);
		logger.info("parkingLevel saved id[" + level.getId() + "]");

		return level;
	}

	/**
	 * Create and save a new level on building with specified parameters
	 */
	@Override
	public ParkingLevel create(int numberOfParkingSpaces, Integer number) {
		logger.debug("call parkingLevel.create params [numberOfParkingSpaces=" + numberOfParkingSpaces + ", number="
				+ number + "]");

		ParkingLevel level = new ParkingLevel(number);
		this.save(level);
		
		// Create a new list of spaces
		List<ParkingSpace> listParkingSpace = new ArrayList<ParkingSpace>();
		for (int i = 1; i <= numberOfParkingSpaces; i++) {
			ParkingSpace parkingSpace = new ParkingSpace(level, i);
			parkingSpaceService.save(parkingSpace);
			listParkingSpace.add(parkingSpace);
		}
		level.setListParkingSpace(listParkingSpace);
		logger.info("parkingLevel created id[" + level.getId() + "]");

		return save(level);
	}

	/**
	 * Return a list of all levels
	 */
	@Override
	public List<ParkingLevel> getAllLevels() {
		logger.debug("call parkingLevel.getAllLevels");
		return parkingLevelRepository.findAll();
	}

	public void deleteAll() {
		logger.debug("call parkingLevel.deleteAll");
		parkingLevelRepository.deleteAll();
		logger.info("call parkingLevel.deletedAll - deleted");
	}

	@Override
	public void createBuilding(Integer levels, Integer spaces) {
		for (int i = 1; i <= levels; i++) {
			this.create(spaces, i);
		}
		
	}

	@Override
	public void fillBuilding(Integer number) {
		for (int i = 0; i < number; i++) {
			String generatedLicensePlate = LicencePlateGenerator.generateLicencePlate();
			try {
				// create a car
				Vehicle vehicle = new Vehicle(TypeVehiclesEnum.getARandomType(), generatedLicensePlate);
				vehicleService.save(vehicle);

				// get a free space
				ParkingSpace space = parkingSpaceService.getAFreeSpace();
				parkingSpaceService.updateToOccupied(space, vehicle);
			} catch (SameLicencePlateException e) {
				logger.info(e.getMessage());
			} catch (NoMoreSpacesException e) {
				logger.info(e.getMessage());
			}

		}		
	}

}
