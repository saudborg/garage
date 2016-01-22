package com.sauloborges.ysura.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * This class represents one floor in the building.
 * 
 * It has its own number and a list of spaces
 * @author sauloborges
 *
 */
@Entity(name = "parkingLevel")
public class ParkingLevel implements Serializable {

	private static final long serialVersionUID = -1968291580299697468L;

	@Id
	@GeneratedValue
	private Integer id;

	private Integer number;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parkingLevel", cascade = CascadeType.ALL)
	private List<ParkingSpace> listParkingSpace = new ArrayList<ParkingSpace>();

	public ParkingLevel(Integer number) {
		this.number = number;
	}

	public ParkingLevel() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ParkingSpace> getListParkingSpace() {
		return listParkingSpace;
	}

	public void setListParkingSpace(List<ParkingSpace> listParkingSpace) {
		this.listParkingSpace = listParkingSpace;
	}

	@Override
	public String toString() {
		return "ParkingLevel [id=" + id + ", number=" + number + "]";
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
