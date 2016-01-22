package com.sauloborges.ysura.domain;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.sauloborges.ysura.util.TimeFormatter;

/**
 * This class represent a space in a floor (level)
 * 
 * It has a number, a vehicle if has one occupying and the time that the vehicle
 * entered
 * 
 * @author sauloborges
 *
 */
@Entity
public class ParkingSpace implements Serializable {

	private static final long serialVersionUID = -4747312402458604533L;

	@Id
	@GeneratedValue
	private Integer id;

	private Integer number;

	@OneToOne
	private Vehicle vehicle;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parking_level_id")
	private ParkingLevel parkingLevel;

	private Calendar timeEnter;

	public ParkingSpace() {
	}

	public ParkingSpace(ParkingLevel level, Integer number) {
		this.parkingLevel = level;
		this.vehicle = null;
		this.number = number;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public ParkingLevel getParkingLevel() {
		return parkingLevel;
	}

	public void setParkingLevel(ParkingLevel parkingLevel) {
		this.parkingLevel = parkingLevel;
	}

	@Override
	public String toString() {
		return "ParkingSpace [id=" + id + ", vehicle=" + vehicle + ", parkingLevel=" + parkingLevel  + "]";
	}

	public Calendar getTimeEnter() {
		return timeEnter;
	}

	public void setTimeEnter(Calendar timeEnter) {
		this.timeEnter = timeEnter;
	}

	public String prettyPrint() {
		if (vehicle != null) {
			return "ParkingSpace [" + id + "], vehicle " + vehicle.getType().getType() + ", licence plate="
					+ vehicle.getLicencePlate() + " - timeEnter=" + TimeFormatter.formatTime(timeEnter) + "]";
		} else {
			return "ParkingSpace [" + id + "] is free";
		}
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getTimeEnterStr() {
		return timeEnter != null ? TimeFormatter.formatTime(timeEnter) : null;
	}

}
