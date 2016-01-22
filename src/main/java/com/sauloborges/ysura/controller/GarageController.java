package com.sauloborges.ysura.controller;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sauloborges.ysura.controller.constants.Pages;
import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;
import com.sauloborges.ysura.service.ParkingSpaceService;
import com.sauloborges.ysura.service.VehicleService;

@Controller
public class GarageController {

	private final Logger logger = LoggerFactory.getLogger(GarageController.class);

	@Autowired
	private ParkingSpaceService spaceService;

	@Autowired
	private VehicleService vehicleService;

	/**
	 * Show vehicle details in garage
	 * 
	 * @param space
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showVehicleInGarage", method = RequestMethod.GET)
	public String showVehicleInGarage(@ModelAttribute("space") ParkingSpace space, Model model) {
		logger.info("accessing url[ /showVehicleInGarage] params[" + space + "]");

		model.addAttribute("space", space);

		return Pages.SHOW_VEHICLE_IN_GARAGE;
	}

	@RequestMapping("/removeVehicle")
	public String updateToFree(@ModelAttribute("space") ParkingSpace space, RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /removeVehicle] params[" + space + "]");

		// Finding a space by Id
		ParkingSpace parkingSpace = spaceService.findById(space.getId());

		// Set time that the vehicle is leaving
		Calendar timeEnter = parkingSpace.getTimeEnter();

		// Set to free the space
		spaceService.updateToFree(parkingSpace);

		// Format to show how long the vehicle stayed
		long total = Calendar.getInstance().getTimeInMillis() - timeEnter.getTimeInMillis();
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(total);
		int min = time.get(Calendar.MINUTE);
		int sec = time.get(Calendar.SECOND);

		redirectAttributes.addFlashAttribute("msgOK",
				"The place is avaible now, your car was for " + String.format("%02d minutes and %02d seconds", min, sec));

		return Pages.REDIRECT_BUILDING;
	}

	/**
	 * Get a free space and put the vehicle in garage
	 * 
	 * @param vehicleParam
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/putInGarage")
	public String putInGarage(@ModelAttribute("vehicle") Vehicle vehicleParam, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /putInGarage] params[" + vehicleParam + "]");

		Vehicle vehicle = vehicleService.findById(vehicleParam.getId());

		try {
			ParkingSpace aFreeSpace = spaceService.getAFreeSpace();
			spaceService.updateToOccupied(aFreeSpace, vehicle);
			redirectAttributes.addFlashAttribute("msgOK",
					"Your car has been placed on " + aFreeSpace.getParkingLevel().getNumber() + " floor on space: "
							+ aFreeSpace.getNumber() + " at " + aFreeSpace.getTimeEnterStr());
		} catch (NoMoreSpacesException e) {
			bindingResult.addError(new ObjectError("noMoreSpaces", e.getMessage()));
			return "createAVehicle";
		}
		
		return Pages.REDIRECT_BUILDING;
	}
}
