package com.fastcampus.schedule.exception;

import com.fastcampus.schedule.exception.constant.ErrorCode;

import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException {

	private ErrorCode errorCode;
	private String message;

	public ScheduleException(ErrorCode errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}
