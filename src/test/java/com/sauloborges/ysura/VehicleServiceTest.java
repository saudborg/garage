package com.sauloborges.ysura;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sauloborges.ysura.domain.TypeVehiclesEnum;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.SameLicencePlateException;
import com.sauloborges.ysura.service.VehicleService;
import com.sauloborges.ysura.util.LicencePlateGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@TestPropertySource(locations = "classpath:test.properties")
public class VehicleServiceTest {

	private VehicleService vehicleService;
	
	@Autowired
	private void setVehicleRepository(VehicleService vehicleRepository) {
		this.vehicleService = vehicleRepository;
	}
	

	private Vehicle createACar() throws SameLicencePlateException {
		return this.createACar(null);
	}

	private Vehicle createACar(String licencePlate) throws SameLicencePlateException {
		if (licencePlate == null)
			licencePlate = LicencePlateGenerator.generateLicencePlate();

		Vehicle car = new Vehicle(TypeVehiclesEnum.CAR, licencePlate);
		return vehicleService.save(car);
	}

	private Vehicle createAMotorbike() throws SameLicencePlateException {
		return this.createAMotorbike(null);
	}

	private Vehicle createAMotorbike(String licencePlate) throws SameLicencePlateException {
		if (licencePlate == null)
			licencePlate = LicencePlateGenerator.generateLicencePlate();

		Vehicle motorbike = new Vehicle(TypeVehiclesEnum.MOTORBIKE, licencePlate);
		return vehicleService.save(motorbike);
	}

	@Test
	public void testSaveACar() throws SameLicencePlateException {

		Vehicle car = this.createACar();
		Vehicle carDatabase = vehicleService.findById(car.getId());

		assertNotNull(carDatabase);
		assertEquals(TypeVehiclesEnum.CAR, car.getType());

		assertEquals(car.getId(), carDatabase.getId());
		assertEquals(car.getLicencePlate(), carDatabase.getLicencePlate());
		assertEquals(car.getType(), carDatabase.getType());
	}

	@Test
	public void testSaveAMotorBike() throws SameLicencePlateException {

		Vehicle motorbike = this.createAMotorbike();
		Vehicle motorbikeDatabase = vehicleService.findById(motorbike.getId());

		assertNotNull(motorbikeDatabase);
		assertEquals(TypeVehiclesEnum.MOTORBIKE, motorbike.getType());

		assertEquals(motorbike.getId(), motorbikeDatabase.getId());
		assertEquals(motorbike.getLicencePlate(), motorbikeDatabase.getLicencePlate());
		assertEquals(motorbike.getType(), motorbikeDatabase.getType());
	}

	@Test
	public void testFindVehicleByLicenceCode() throws SameLicencePlateException {

		Vehicle car = this.createACar();
		Vehicle carDatabase = vehicleService.findByLicencePlate(car.getLicencePlate());

		assertNotNull(carDatabase);
		assertEquals(TypeVehiclesEnum.CAR, car.getType());

		assertEquals(car.getId(), carDatabase.getId());
		assertEquals(car.getLicencePlate(), carDatabase.getLicencePlate());
		assertEquals(car.getType(), carDatabase.getType());
	}

	@Test(expected = SameLicencePlateException.class)
	public void testIsLicencePlateValid() throws SameLicencePlateException {

		Vehicle car = this.createAMotorbike("AAA-111");
		Vehicle carDatabase = vehicleService.findByLicencePlate(car.getLicencePlate());
		assertNotNull(carDatabase);

		Vehicle motorbike = new Vehicle(TypeVehiclesEnum.MOTORBIKE, "AAA-111");
		assertEquals(false, vehicleService.isLicencePlateValid("AAA-111"));

		vehicleService.save(motorbike); // throw an exception

	}

}
