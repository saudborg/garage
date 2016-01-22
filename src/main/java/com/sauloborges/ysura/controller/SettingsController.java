package com.sauloborges.ysura.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sauloborges.ysura.ApplicationConfiguration;
import com.sauloborges.ysura.controller.constants.Pages;
import com.sauloborges.ysura.controller.form.SettingsForm;
import com.sauloborges.ysura.service.ParkingLevelService;

@Controller
public class SettingsController {

	private final Logger logger = LoggerFactory.getLogger(SettingsController.class);

	@Autowired
	private ApplicationConfiguration config;

	@Autowired
	private ParkingLevelService levelService;

	/**
	 * Create a new form settings using the variables configured.
	 * 
	 * Default values could be set on application.properties
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/settings")
	public String settings(Model model) {
		logger.info("accessing url[ /settings]");

		SettingsForm form = new SettingsForm();
		form.setLevels(config.getLevels());
		form.setSpaces(config.getSpaces());
		model.addAttribute("settingsForm", form);

		return Pages.SETTINGS;
	}

	/**
	 * This method receives from template a form with informations about the new
	 * settings on the building.
	 * 
	 * It will remove all vehicles from garage
	 * 
	 * @param settings
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/saveSettings", method = RequestMethod.POST)
	public String saveSettings(@ModelAttribute SettingsForm settings, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("accessing url[ /building] param[" + settings + "]");

		// remove all vehicles from garage
		levelService.deleteAll();

		// setting a new configuration
		config.setLevels(settings.getLevels());
		config.setSpaces(settings.getSpaces());

		// creating a number specified of levels in building
		levelService.createBuilding(settings.getLevels(), settings.getSpaces());

		// Fill spaces in a garage that have been in the setting screen
		if (settings.getFillSpaces() != null && settings.getFillSpaces() > 0) {
			levelService.fillBuilding(settings.getFillSpaces());
		}

		redirectAttributes.addFlashAttribute("msgOK", "Your settings has been changed to " + config.getLevels()
				+ " levels and " + config.getSpaces() + " spaces");

		return Pages.REDIRECT_SETTINGS;
	}
}
