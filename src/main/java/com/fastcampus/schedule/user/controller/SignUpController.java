package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.controller.requset.SignUpRequest;
import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Api(tags = "회원가입")
@RestController
@RequiredArgsConstructor
public class SignUpController {

	private final UserService userService;

	@PostMapping("/signup")
	public HttpEntity<Void> SignUp(@RequestBody @Valid SignUpRequest request) {
		userService.signUp(request);
		return ResponseEntity.ok(null);
	}

}
