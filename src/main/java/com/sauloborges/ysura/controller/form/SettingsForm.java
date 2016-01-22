package com.sauloborges.ysura.controller.form;

public class SettingsForm {

	private Integer levels;

	private Integer spaces;
	
	private Integer fillSpaces;

	public SettingsForm() {
	}

	public SettingsForm(Integer levels, Integer spaces, Integer fillSpaces) {
		super();
		this.levels = levels;
		this.spaces = spaces;
		this.fillSpaces = fillSpaces;
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

	public Integer getFillSpaces() {
		return fillSpaces;
	}

	public void setFillSpaces(Integer fillSpaces) {
		this.fillSpaces = fillSpaces;
	}

	@Override
	public String toString() {
		return "SettingsForm [levels=" + levels + ", spaces=" + spaces + ", fillSpaces=" + fillSpaces + "]";
	}

}
