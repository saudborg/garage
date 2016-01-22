package com.sauloborges.ysura.exception;

/**
 * Throw this exception when the system try to save a new vehicle with a licence plate that it is already created
 * @author sauloborges
 *
 */
public class SameLicencePlateException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameLicencePlateException(String licencePlate) {
		super("Licence Plate [" + licencePlate + "] is already created");
	}

}
