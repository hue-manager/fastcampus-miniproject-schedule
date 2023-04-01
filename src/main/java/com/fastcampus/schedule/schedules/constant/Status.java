package com.fastcampus.schedule.schedules.constant;

public enum Status {
	WAITING("대기"),
	PERMIT("승인")
	;

	private String description;

	Status(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
