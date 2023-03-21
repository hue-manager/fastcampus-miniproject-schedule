package com.fastcampus.schedule.exception.constant;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	// TODO message
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, ""),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, ""),
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ""),

	;

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
