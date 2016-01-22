package com.sauloborges.ysura.controller;

import javax.validation.Valid;

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
import com.sauloborges.ysura.controller.form.LicencePlateForm;
import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.domain.TypeVehiclesEnum;
import com.sauloborges.ysura.domain.Vehicle;
import com.sauloborges.ysura.exception.NoMoreSpacesException;
import com.sauloborges.ysura.exception.SameLicencePlateException;
import com.sauloborges.ysura.service.ParkingSpaceService;
import com.sauloborges.ysura.service.VehicleService;
import com.sauloborges.ysura.util.LicencePlateGenerator;

@Controller
public class VehicleController {

	private final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	@Autowired
	private ParkingSpaceService spaceService;

	@Autowired
	private VehicleService vehicleService;

	private LicencePlateGenerator licencePlateGenerator;

	/**
	 * This method represents the main page for vehicles. Starting in this page
	 * you can see if the vehicle exists, put it or take it out and also create
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/vehicle")
	public String home(Model model) {
		logger.info("accessing url[ /vehicle]");

		LicencePlateForm licencePlateForm = new LicencePlateForm();
		model.addAttribute("licencePlateForm", licencePlateForm);

		return Pages.VEHICLE;
	}

	/**
	 * This method sees if the vehicle exists and is on garage, if not, verify
	 * if exists and finally if doesn't exists redir to a page to create it
	 * 
	 * @param form
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/searchVehicle", method = RequestMethod.POST)
	public String searchVehicle(@ModelAttribute LicencePlateForm form, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /searchVehicle] params[" + form + "]");

		ParkingSpace space = spaceService.findVehicleInAParkingSpaceByLicencePlate(form.getLicencePlate());

		if (space != null && space.getVehicle() != null) {
			logger.info("The vehicle [" + space.getVehicle() + "] is on garage vehicle");
			redirectAttributes.addFlashAttribute("space", space);

			return Pages.REDIRECT_SHOW_VEHICLE_IN_GARAGE;
		} else {

			Vehicle vehicle = vehicleService.findByLicencePlate(form.getLicencePlate());

			if (vehicle != null) {
				logger.debug("The vehicle [" + vehicle + "] is not in garage");
				redirectAttributes.addFlashAttribute("vehicle", vehicle);

				return "redirect:/detailsVehicle";
			} else {
				logger.debug("The vehicle [licencePlate=" + form.getLicencePlate() + "] doesn't exists in our system");

				redirectAttributes.addFlashAttribute("licencePlate", form.getLicencePlate());
				redirectAttributes.addFlashAttribute("msgCreate",
						"We don't have this vehicle in our system, you can add it");

				return Pages.REDIRECT_CREATE_A_VEHICLE;
			}
		}
	}

	@RequestMapping(value = "/detailsVehicle", method = RequestMethod.GET)
	public String showDetailsVehicle(@ModelAttribute("vehicle") Vehicle vehicle, Model model) {
		logger.info("accessing url[ /detailsVehicle] params[" + vehicle + "]");

		model.addAttribute("vehicle", vehicle);

		return Pages.DETAILS_VEHICLE;
	}

	/**
	 * Create a form to be possible create a new Vehicle
	 * 
	 * @param licencePlate
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/createAVehicle", method = RequestMethod.GET)
	public String createAVehicle(@ModelAttribute("licencePlate") String licencePlate, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /createAVehicle] licencePlate[" + licencePlate + "]");

		Vehicle vehicle = new Vehicle();
		if (licencePlate != null)
			vehicle.setLicencePlate(licencePlate);

		model.addAttribute("licencePlate", licencePlate);
		model.addAttribute("generateLicencePlate", licencePlateGenerator);
		model.addAttribute("types", TypeVehiclesEnum.values());

		model.addAttribute("vehicle", vehicle);

		return Pages.CREATE_A_VEHICLE;
	}

	/**
	 * Save a Vehicle
	 * 
	 * @param vehicle
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/saveVehicle", method = RequestMethod.POST)
	public String saveVehicle(@Valid Vehicle vehicle, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /saveVehicle] params[" + vehicle + "]");

		// Verify if has errors in validation
		if (bindingResult.hasErrors()) {
			return Pages.CREATE_A_VEHICLE;
		}

		try {
			vehicleService.save(vehicle);
			ParkingSpace freeSpace = spaceService.getAFreeSpace();
			spaceService.updateToOccupied(freeSpace, vehicle);
			redirectAttributes.addFlashAttribute("msgOK",
					"Your car has been placed " + freeSpace.getParkingLevel().getNumber() + " floor on space: "
							+ freeSpace.getNumber() + " at " + freeSpace.getTimeEnterStr());
		} catch (SameLicencePlateException e) {
			bindingResult.addError(new ObjectError("sameLicencePlate", e.getMessage()));
			return Pages.CREATE_A_VEHICLE;
		} catch (NoMoreSpacesException e) {
			bindingResult.addError(new ObjectError("noMoreSpaces", e.getMessage()));
			return Pages.CREATE_A_VEHICLE;
		}

		return Pages.REDIRECT_BUILDING;
	}

}
