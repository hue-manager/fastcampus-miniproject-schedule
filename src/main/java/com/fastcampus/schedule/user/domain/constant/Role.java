package com.fastcampus.schedule.user.domain.constant;

public enum Role {
	ROLE_USER("일반"),
	ROLE_ADMIN("관리자"),
	DEFAULT("대기");

	private String description;

	Role(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
