package com.sauloborges.ysura.service;

import java.util.List;

import com.sauloborges.ysura.domain.ParkingLevel;

public interface ParkingLevelService {

	public ParkingLevel save(ParkingLevel level);

	public List<ParkingLevel> getAllLevels();

	public ParkingLevel create(int numberOfParkingSpaces, Integer number);

	public void deleteAll();
	
	public void createBuilding(Integer levels, Integer spaces);
	
	public void fillBuilding(Integer number);

}
