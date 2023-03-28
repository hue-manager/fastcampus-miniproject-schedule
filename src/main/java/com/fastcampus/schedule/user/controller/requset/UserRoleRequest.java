package com.fastcampus.schedule.user.controller.requset;

import javax.validation.constraints.NotNull;

import com.fastcampus.schedule.user.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequest {

	@NotNull
	private Role role;

	public UserRoleRequest(){
		this.role = role;
	}

	public UserRoleRequest(Role role) {
		this.role = role;
	}
}
