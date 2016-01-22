package com.sauloborges.ysura.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This enum represents a type of vehicle
 * @author sauloborges
 *
 */
public enum TypeVehiclesEnum {

	CAR(1, "Car"), //
	MOTORBIKE(2, "Motorbike");

	private Integer id;
	private String type;

	private static final Random R = new Random();
	private static final List<TypeVehiclesEnum> LIST = Collections.unmodifiableList(Arrays.asList(values()));

	private TypeVehiclesEnum(Integer id, String type) {
		this.id = id;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	/**
	 * Get a random type 
	 * @return
	 */
	public static TypeVehiclesEnum getARandomType() {
		return LIST.get(R.nextInt(LIST.size()));
	}

}
