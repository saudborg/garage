package com.sauloborges.ysura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * This class represent the system via console. Here you can Find a Vehicle, Add
 * a Car, Add a Motorbike, Take out a vehicle from garage and search a vehicle
 * by licence plate
 * 
 * @author sauloborges
 *
 */
@Component
@Transactional
public class ApplicationRunner implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

	@Autowired
	private ParkingLevelService levelService;

	@Autowired
	private ParkingSpaceService spaceService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ApplicationConfiguration configuration;

	@Override
	public void run(String... arg0) throws Exception {

		logger.info("Starting application.");
		System.out.println("Starting the application with default parameters: " + configuration.getLevels()
				+ " levels and " + configuration.getSpaces() + " spaces per level");
		int levels = configuration.getLevels();
		int spaces = configuration.getSpaces();

		logger.debug("Creating all levels in the building");
		for (int i = 1; i <= levels; i++) {
			levelService.create(spaces, i);
		}
		logger.info(levels + " levels created");

		logger.debug("ExecuteOnMain=" + configuration.isExecuteOnMain());
		if (configuration.isExecuteOnMain()) {

			logger.debug("Creating a random vehicles");
			for (int i = 0; i < configuration.getSpacesOccupied(); i++) {
				String generatedLicensePlate = LicencePlateGenerator.generateLicencePlate();
				try {
					// create a car
					Vehicle vehicle = new Vehicle(TypeVehiclesEnum.getARandomType(), generatedLicensePlate);
					vehicleService.save(vehicle);

					// get a free space
					ParkingSpace space = spaceService.getAFreeSpace();
					spaceService.updateToOccupied(space, vehicle);

				} catch (Exception e) {
					logger.info("You can create a car with the same license plate: " + generatedLicensePlate);
					System.out.println("You can create a car with the same license plate: " + generatedLicensePlate);
				}
			}

			// iteration with a user
			while (true) {
				try {
					// Menu
					System.out.println("Please select your option (Just numbers)");
					System.out.println("\n\n\n1 - ) Find a car");
					System.out.println("2 - ) Add a car");
					System.out.println("3 - ) Add a motorbike");
					System.out.println("4 - ) Remove a vehicle");
					System.out.println("5 - ) Get a vehicle");

					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					Integer next = Integer.valueOf(br.readLine());
					switch (next) {
					case 1:
						findACarInBuilding(br);
						break;
					case 2:
						addACar(br);
						break;
					case 3:
						addAMotorbike(br);
						break;
					case 4:
						takeOutAVehicle(br);
					case 5:
						getAVehicle(br);
					default:
						break;
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

				// Show the current status on building
				System.out.println("\n\n\n##################################");
				System.out.println("Current status on building");
				List<ParkingLevel> building = levelService.getAllLevels();
				for (ParkingLevel parkingLevel : building) {
					System.out.println("Level " + parkingLevel.getId());
					List<ParkingSpace> listParkingSpace = parkingLevel.getListParkingSpace();
					for (ParkingSpace parkingSpace : listParkingSpace) {
						System.out.println(parkingSpace.prettyPrint());
					}
				}

			}
		}

	}

	/**
	 * This method receive a licence plate (String) and sees if the vehicle is
	 * in any space on building, if not, it will search to see if the vehicle is
	 * on the system but outside from garage. If it find, ask for the user if
	 * would like to put in garage.
	 * 
	 * @throws Exception
	 */
	private void getAVehicle(BufferedReader br) throws Exception {
		System.out.print("Enter a licence plate: ");
		String licencePlate = br.readLine();

		ParkingSpace space = spaceService.findVehicleInAParkingSpaceByLicencePlate(licencePlate);
		if (space != null) {
			System.out.println("Your " + space.getVehicle().getType().getType() + " is on "
					+ space.getParkingLevel().getId() + " on space: " + space.getId());
		} else {
			Vehicle findByLicencePlate = vehicleService.findByLicencePlate(licencePlate);
			System.out.println("We found your vehicle in your system. Would you like to put in?");
			String sORn = br.readLine();
			if (sORn == "s") {
				ParkingSpace newSpace = spaceService.getAFreeSpace();
				spaceService.updateToOccupied(newSpace, findByLicencePlate);
			} else {
				System.out.println("OK, we won't put in. Thank you");
			}
		}

	}

	/**
	 * This method receives a licence plate. If the vehicle exists and it is on
	 * some space, will take it out and put the space as available
	 * 
	 * @throws IOException
	 */
	private void takeOutAVehicle(BufferedReader br) throws IOException {
		System.out.print("Enter a licence plate: ");
		String licencePlate = br.readLine();
		ParkingSpace space = spaceService.findVehicleInAParkingSpaceByLicencePlate(licencePlate);
		if (space != null) {
			spaceService.updateToFree(space);
			System.out.println(space);
		} else {
			System.out.println("This vehicle is not in the garage");
		}
	}

	/**
	 * This method receives a licence plate from user and creates a new car
	 * 
	 * @throws SameLicencePlateException
	 * @throws NoMoreSpacesException
	 * @throws IOException
	 */
	private void addACar(BufferedReader br) throws SameLicencePlateException, NoMoreSpacesException, IOException {
		System.out.print("Enter a licence plate: ");
		String licencePlate = br.readLine();

		Vehicle car = new Vehicle(TypeVehiclesEnum.CAR, licencePlate);
		vehicleService.save(car);

		// get a free space
		ParkingSpace space = spaceService.getAFreeSpace();
		spaceService.updateToOccupied(space, car);
	}

	/**
	 * This method receives a licence plate from user and creates a new
	 * motorbike
	 * 
	 * @throws SameLicencePlateException
	 * @throws NoMoreSpacesException
	 * @throws IOException
	 */
	private void addAMotorbike(BufferedReader br) throws SameLicencePlateException, NoMoreSpacesException, IOException {
		System.out.print("Enter a licence plate: ");
		String licencePlate = br.readLine();
		Vehicle motorbike = new Vehicle(TypeVehiclesEnum.MOTORBIKE, licencePlate);
		vehicleService.save(motorbike);

		// get a free space
		ParkingSpace space = spaceService.getAFreeSpace();
		spaceService.updateToOccupied(space, motorbike);
	}

	/**
	 * This method receives a licence plate and will search if the car is on the building.
	 * @param br
	 * @throws IOException
	 */
	private void findACarInBuilding(BufferedReader br) throws IOException {
		System.out.print("Enter a licence plate: ");
		String licencePlate = br.readLine();

		ParkingSpace space = spaceService.findVehicleInAParkingSpaceByLicencePlate(licencePlate);
		if (space != null)
			System.out.println("Your " + space.getVehicle().getType().getType() + " is on "
					+ space.getParkingLevel().getId() + " on space: " + space.getId());
		else {
			System.out.println("This vehicle is not in the garage");
		}
	}

}
