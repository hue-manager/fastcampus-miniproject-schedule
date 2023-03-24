package com.fastcampus.schedule.user.controller.requset;

import com.fastcampus.schedule.user.constant.Role;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRoleRequest {

<<<<<<< HEAD:src/main/java/com/fastcampus/schedule/user/controller/requset/UserRoleRequest.java
	private Role role;
=======
    @NotEmpty
    private Role role;
>>>>>>> user:src/main/java/com/fastcampus/schedule/user/domain/UserRoleRequest.java

	public UserRoleRequest(Role role) {
		this.role = role;
	}
}
