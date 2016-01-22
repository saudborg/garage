package com.sauloborges.ysura.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class represents a vehicle (could be Car or motorbike) and your own licencePlate
 * @author sauloborges
 *
 */
@Entity
public class Vehicle implements Serializable {

	private static final long serialVersionUID = -8892223902280877378L;

	@Id
	@GeneratedValue
	private Integer id;

	@NotNull(message="Licence Plate can not be null")
	@NotBlank(message="Licence Plate can not be blank")
	private String licencePlate;

	@NotNull(message="Type of Vehicle can not be null")
	private TypeVehiclesEnum type;

	public Vehicle() {
	}

	public Vehicle(TypeVehiclesEnum type) {
		this.type = type;
	}

	public Vehicle(TypeVehiclesEnum type, String licencePlate) {
		this.type = type;
		this.licencePlate = licencePlate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicencePlate() {
		return licencePlate;
	}

	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}

	public TypeVehiclesEnum getType() {
		return type;
	}

	public void setType(TypeVehiclesEnum type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", licencePlate=" + licencePlate + ", type=" + type + "]";
	}
	
}
