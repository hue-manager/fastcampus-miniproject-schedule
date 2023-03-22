package com.fastcampus.schedule.schedules.controller.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleRequest {

	@NotNull
	private String category;
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;
	@Length(max = 200)
	private String memo;

	public static Schedule toEntity(ScheduleRequest request, User user) {
		return Schedule.of(user,
						   Category.valueOf(request.category),
						   request.startDate,
						   request.endDate,
						   request.memo);
	}
}
