package com.fastcampus.schedule.user.controller.requset;

import com.fastcampus.schedule.user.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequest {

	private Role role;

	public UserRoleRequest(Role role) {
		this.role = role;
	}
}
