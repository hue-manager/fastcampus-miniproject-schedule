package com.fastcampus.schedule.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SignUpController {

	private final UserService userService;

	@PostMapping("/signUp")
	public void SignUp(@RequestBody User user) throws Exception {
		userService.signUp(user);
	}

}
