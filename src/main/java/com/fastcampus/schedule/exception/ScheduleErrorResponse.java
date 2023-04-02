package com.fastcampus.schedule.exception;

import com.fastcampus.schedule.exception.constant.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleErrorResponse {

	private ErrorCode errorCode;
	private String message;
}
