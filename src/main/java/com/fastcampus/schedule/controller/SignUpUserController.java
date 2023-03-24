package com.fastcampus.schedule.controller;

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
public class SignUpUserController {

	private final UserService userService;

	//    @GetMapping("/signUp")
	//    public String signup() {
	//        return "signup_form";
	//    }

	@PostMapping("/signUp")
	public void SignUp(@RequestBody User user) throws Exception {
		userService.signUp(user);
	}

}
