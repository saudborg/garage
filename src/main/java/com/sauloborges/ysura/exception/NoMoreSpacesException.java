package com.sauloborges.ysura.exception;

/**
 * Throw this exception when the garage is full and don't have space enough to put one more vehicle
 * @author sauloborges
 *
 */
public class NoMoreSpacesException extends Exception {

	private static final long serialVersionUID = -5819384067652526052L;

	public NoMoreSpacesException() {
		super("Sorry, we don't have more spaces in our garage");
	}

}
