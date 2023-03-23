package com.fastcampus.schedule.user.domain;

import java.util.List;

import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.user.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

	private String email;
	private String userName;
	private Role role;
	private List<Schedule> schedules;
	private List<LoginLog> logs;
}
