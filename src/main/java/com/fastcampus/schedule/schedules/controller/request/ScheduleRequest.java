package com.fastcampus.schedule.schedules.controller.request;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.user.User;
import com.fastcampus.schedule.util.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleRequest {

	private Category category;
	private String startDate;
	private String endDate;
	private String memo;

	public static Schedule toEntity(ScheduleRequest request, User user) {
		return Schedule.of(user,
						   request.category,
						   Utils.stringToDate(request.startDate),
						   Utils.stringToDate(request.endDate),
						   request.memo);
	}
}
