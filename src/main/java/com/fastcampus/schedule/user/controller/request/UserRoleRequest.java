package com.fastcampus.schedule.user.controller.request;

import javax.validation.constraints.NotNull;

import com.fastcampus.schedule.user.domain.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequest {

	@NotNull
	private Role role;

	public UserRoleRequest() {
		this.role = role;
	}

	public UserRoleRequest(Role role) {
		this.role = role;
	}
}
