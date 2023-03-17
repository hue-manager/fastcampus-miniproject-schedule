package com.fastcampus.schedule.domain.constant;

public enum Category {
	VACATION("연차"),
	WORK("당직");

	private String description;

	Category(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
