package com.sauloborges.ysura.service;

import java.util.List;

import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;

public interface ParkingSpaceService {

	public ParkingSpace save(ParkingSpace parkingSpace);

	public List<ParkingSpace> getList();

	public ParkingSpace updateToOccupied(ParkingSpace parkingSpace, Vehicle vehicle);

	public ParkingSpace updateToFree(ParkingSpace parkingSpace);
	
	public List<ParkingSpace> getFreeSpaces();
	
	public ParkingSpace getAFreeSpace() throws NoMoreSpacesException;
	
	public ParkingSpace findVehicleInAParkingSpaceByLicencePlate(String licencePlate);

	public ParkingSpace findById(Integer id);
	
}
