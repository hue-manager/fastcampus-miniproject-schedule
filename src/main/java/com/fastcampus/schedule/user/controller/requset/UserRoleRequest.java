package com.fastcampus.schedule.user.controller.requset;

import javax.validation.constraints.NotEmpty;

import com.fastcampus.schedule.user.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequest {

	@NotEmpty
	private Role role;

	public UserRoleRequest(Role role) {
		this.role = role;
	}
}
