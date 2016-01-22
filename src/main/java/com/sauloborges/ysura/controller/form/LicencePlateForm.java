package com.sauloborges.ysura.controller.form;

public class LicencePlateForm {

	private String licencePlate;

	public LicencePlateForm() {
	}

	public LicencePlateForm(String licencePlate) {
		super();
		this.licencePlate = licencePlate;
	}

	public String getLicencePlate() {
		return licencePlate;
	}

	public void setLicencePlate(String licencePlate) {
		this.licencePlate = licencePlate;
	}

	@Override
	public String toString() {
		return "LicencePlateForm [licencePlate=" + licencePlate + "]";
	}

}
