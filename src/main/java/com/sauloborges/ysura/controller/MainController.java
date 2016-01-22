package com.sauloborges.ysura.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sauloborges.ysura.controller.constants.Pages;
import com.sauloborges.ysura.domain.ParkingLevel;
import com.sauloborges.ysura.domain.ParkingSpace;
import com.sauloborges.ysura.service.ParkingLevelService;
import com.sauloborges.ysura.service.ParkingSpaceService;

@Controller
public class MainController {

	private final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private ParkingSpaceService spaceService;

	@Autowired
	private ParkingLevelService levelService;

	@RequestMapping("/")
	public String home(Model model, RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /]");
		return Pages.REDIRECT_BUILDING; // "redirect:/building";
	}

	/**
	 * Get all free spaces in the garage and all the garage to show current
	 * details about the garage
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/building")
	public String showBuilding(Model model, RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /building]");

		List<ParkingSpace> freeSpaces = spaceService.getFreeSpaces();
		// Show how many free space have
		model.addAttribute("numberFreeSpaces", freeSpaces.size());

		// Show all levels and their respective spaces being occupied by a
		// vehicle or not
		List<ParkingLevel> building = levelService.getAllLevels();
		model.addAttribute("building", building);

		return Pages.BUILDING; // "building";
	}

}
