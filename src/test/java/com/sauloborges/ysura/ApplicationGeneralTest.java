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
public class ApplicationGeneralTest {

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

	private Vehicle createACar(String licencePlate) throws SameLicencePlateException {
		if (licencePlate == null)
			licencePlate = LicencePlateGenerator.generateLicencePlate();

		Vehicle car = new Vehicle(TypeVehiclesEnum.CAR, licencePlate);
		return vehicleService.save(car);
	}

	private void createABuilding(Integer levels, Integer spaces) {
		parkingLevelService.deleteAll();
		parkingLevelService.createBuilding(levels, spaces);
	}
	

	@Test
	public void testCreateBuilding() {
		int levels = config.getLevels();
		int spaces = config.getSpaces();

		createABuilding(levels, spaces);
		List<ParkingSpace> list = parkingSpaceService.getList();

		assertNotNull(list);
		assertEquals(levels * spaces, list.size());
	}

	@Test
	public void testFillBuilding() {
		int levels = config.getLevels();
		int spaces = config.getSpaces();

		createABuilding(levels, spaces);

		parkingLevelService.fillBuilding(levels * spaces);
		List<ParkingSpace> freeSpaces = parkingSpaceService.getFreeSpaces();

		assertNotNull(freeSpaces);
		assertEquals(0, freeSpaces.size());
	}

	@Test(expected = NoMoreSpacesException.class)
	public void testNoMoreSpaces() throws SameLicencePlateException, NoMoreSpacesException {
		int levels = config.getLevels();
		int spaces = config.getSpaces();

		createABuilding(levels, spaces);

		parkingLevelService.fillBuilding(levels * spaces);

		parkingSpaceService.getAFreeSpace(); // throws excpetion
	}

	@Test
	public void testCompelte() throws SameLicencePlateException, NoMoreSpacesException {
		int levels = config.getLevels();
		int spaces = config.getSpaces();

		createABuilding(levels, spaces);

		parkingLevelService.fillBuilding((levels * spaces) - 1);

		this.createACar("TTT-222");
		Vehicle vehicle = vehicleService.findByLicencePlate("TTT-222");
		assertEquals("TTT-222", vehicle.getLicencePlate());

		ParkingSpace originalSpace = parkingSpaceService.getAFreeSpace();
		parkingSpaceService.updateToOccupied(originalSpace, vehicle);

		List<ParkingSpace> freeSpaces = parkingSpaceService.getFreeSpaces();
		assertEquals(0, freeSpaces.size());

		ParkingSpace spaceOccupied = parkingSpaceService
				.findVehicleInAParkingSpaceByLicencePlate(vehicle.getLicencePlate());
		assertNotNull(spaceOccupied.getVehicle());
		assertEquals(vehicle.getId(), spaceOccupied.getVehicle().getId());
		assertEquals(vehicle.getLicencePlate(), spaceOccupied.getVehicle().getLicencePlate());
		assertEquals(originalSpace.getId(), spaceOccupied.getId());
		assertEquals(originalSpace.getParkingLevel().getNumber(), spaceOccupied.getParkingLevel().getNumber());
		assertEquals(originalSpace.getNumber(), spaceOccupied.getNumber());
		assertNotNull(spaceOccupied.getTimeEnter());

		ParkingSpace spaceFree = parkingSpaceService.updateToFree(spaceOccupied);
		assertEquals(originalSpace.getId(), spaceFree.getId());
		assertEquals(originalSpace.getNumber(), spaceFree.getNumber());
		assertNull(spaceFree.getTimeEnter());
	}

}
