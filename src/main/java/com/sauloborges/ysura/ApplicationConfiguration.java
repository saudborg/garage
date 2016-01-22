package com.sauloborges.ysura;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class represents all variables you can configure in
 * application.properties
 * 
 * @author sauloborges
 *
 */
@Component
public class ApplicationConfiguration {

	private static Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

	/**
	 * true if you would like to see the application on console
	 */
	@Value("${main}")
	private boolean executeOnMain;

	/**
	 * Number of levels you would like to see in your building
	 */
	@Value("${main.levels}")
	private Integer levels;

	/**
	 * Number of spaces per level you would like to see in your building
	 */
	@Value("${main.levels.space}")
	private Integer spaces;

	/**
	 * Number of spaces that the system will fill for you before you start
	 */
	@Value("${main.spaces.fit}")
	private Integer spacesOccupied;

	@PostConstruct
	public void postConstruct() {
		logger.debug("executeOnMain: " + executeOnMain);
		logger.debug("levels: " + levels);
		logger.debug("spaces: " + spaces);
		logger.debug("spacesOccupied: " + spacesOccupied);
	}

	public boolean isExecuteOnMain() {
		return executeOnMain;
	}

	public void setExecuteOnMain(boolean executeOnMain) {
		this.executeOnMain = executeOnMain;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public Integer getSpaces() {
		return spaces;
	}

	public void setSpaces(Integer spaces) {
		this.spaces = spaces;
	}

	public Integer getSpacesOccupied() {
		return spacesOccupied;
	}

	public void setSpacesOccupied(Integer spacesOccupied) {
		this.spacesOccupied = spacesOccupied;
	}

}
