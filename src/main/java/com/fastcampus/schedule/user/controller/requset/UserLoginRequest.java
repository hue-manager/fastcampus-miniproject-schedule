package com.fastcampus.schedule.user.controller.requset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequest {
	private String email;
	private String password;
}
