package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.controller.request.SignUpRequest;
import com.fastcampus.schedule.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "회원가입")
@RestController
@RequiredArgsConstructor
public class SignUpController {

	public static final String SIGN_UP_SUCCESS = "회원가입 성공";
	private final UserService userService;

	@PostMapping("/signup")
	public HttpEntity<String> SignUp(@RequestBody @Valid SignUpRequest request) {
		userService.signUp(request);
		return ResponseEntity.ok(SIGN_UP_SUCCESS);
	}

}
