package com.sauloborges.ysura;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sauloborges.ysura.domain.ParkingLevel;
import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.TypeVehiclesEnum;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;
import com.sauloborges.ysura.exception.SameLicencePlateException;
import com.sauloborges.ysura.service.ParkingLevelService;
import com.sauloborges.ysura.service.ParkingSpaceService;
import com.sauloborges.ysura.service.VehicleService;
import com.sauloborges.ysura.util.LicencePlateGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@TestPropertySource(locations = "classpath:test.properties")
public class ParkingSpaceServiceTest {

	private ParkingSpaceService parkingSpaceService;
	private ParkingLevelService parkingLevelService;
	private VehicleService vehicleService;
	private ApplicationConfiguration config;

	@Autowired
	private void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
		this.parkingSpaceService = parkingSpaceService;
	}

	@Autowired
	private void setParkingLevelService(ParkingLevelService parkingLevelService) {
		this.parkingLevelService = parkingLevelService;
	}

	@Autowired
	private void setConfig(ApplicationConfiguration config) {
		this.config = config;
	}

	@Autowired
	public void setVehicleService(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	private ParkingSpace createASpace() {
		ParkingSpace space = new ParkingSpace();
		space.setNumber(0);
		ParkingLevel parkingLevel = this.createAParkingLevel();
		space.setParkingLevel(parkingLevel);
		return parkingSpaceService.save(space);
	}

	private Vehicle createACar(String licencePlate) throws SameLicencePlateException {
		if (licencePlate == null)
			licencePlate = LicencePlateGenerator.generateLicencePlate();

		Vehicle car = new Vehicle(TypeVehiclesEnum.CAR, licencePlate);
		return vehicleService.save(car);
	}

	private ParkingLevel createAParkingLevel() {
		ParkingLevel level = parkingLevelService.create(config.getSpaces(), 0);
		return level;
	}

	@Test
	public void testSaveASpace() throws SameLicencePlateException {

		ParkingSpace space = this.createASpace();
		ParkingSpace spaceDatabase = parkingSpaceService.findById(space.getId());

		assertNotNull(spaceDatabase);

		assertEquals(spaceDatabase.getId(), spaceDatabase.getId());
		assertEquals(space.getNumber(), spaceDatabase.getNumber());
	}

	@Test
	public void testUpdateToOccupied() throws SameLicencePlateException, NoMoreSpacesException {
		String licencePlate = LicencePlateGenerator.generateLicencePlate();
		Vehicle car = this.createACar(licencePlate);

		ParkingSpace parkingSpace = parkingSpaceService.getAFreeSpace();
		ParkingSpace occupied = parkingSpaceService.updateToOccupied(parkingSpace, car);

		assertNotNull(occupied.getTimeEnter());

		assertEquals(parkingSpace.getId(), occupied.getId());
		assertEquals(parkingSpace.getNumber(), occupied.getNumber());
		assertEquals(parkingSpace.getParkingLevel().getId(), occupied.getParkingLevel().getId());
		assertEquals(licencePlate, occupied.getVehicle().getLicencePlate());
	}

	@Test
	public void testUpdateToFree() throws SameLicencePlateException, NoMoreSpacesException {
		String licencePlate = LicencePlateGenerator.generateLicencePlate();
		Vehicle car = this.createACar(licencePlate);

		ParkingSpace parkingSpace = parkingSpaceService.getAFreeSpace();
		ParkingSpace occupied = parkingSpaceService.updateToOccupied(parkingSpace, car);

		assertNotNull(occupied.getTimeEnter());
		assertEquals(parkingSpace.getId(), occupied.getId());

		ParkingSpace free = parkingSpaceService.updateToFree(occupied);
		assertEquals(occupied.getId(), free.getId());
		assertNull(free.getVehicle());
		assertNull(free.getTimeEnter());
	}

	@Test
	public void testFindVehicleByLicenceCode() throws SameLicencePlateException, NoMoreSpacesException {

		String licencePlate = LicencePlateGenerator.generateLicencePlate();
		Vehicle car = this.createACar(licencePlate);

		ParkingSpace parkingSpace = parkingSpaceService.getAFreeSpace();
		parkingSpaceService.updateToOccupied(parkingSpace, car);

		ParkingSpace parkSpace = parkingSpaceService.findVehicleInAParkingSpaceByLicencePlate(licencePlate);

		assertNotNull(parkSpace);
		assertEquals(car.getId(), parkSpace.getVehicle().getId());
		assertEquals(car.getLicencePlate(), parkSpace.getVehicle().getLicencePlate());
	}

	@Test
	public void testgetAll() throws SameLicencePlateException {

		ParkingSpace space = this.createASpace();

		boolean onFreeSpace = false;

		List<ParkingSpace> freeSpaces = parkingSpaceService.getFreeSpaces();
		for (ParkingSpace parkingSpace : freeSpaces) {
			if (parkingSpace.getId() == space.getId())
				onFreeSpace = true;
		}

		assertEquals(true, onFreeSpace);

		boolean onList = false;

		List<ParkingSpace> list = parkingSpaceService.getList();
		for (ParkingSpace parkingSpace : list) {
			if (parkingSpace.getId() == space.getId())
				onList = true;
		}

		assertEquals(true, onList);

	}

}
