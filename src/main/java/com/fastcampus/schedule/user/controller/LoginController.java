package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.controller.requset.UserLoginRequest;
import com.fastcampus.schedule.user.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/login")
	public HttpEntity<String> login(@RequestBody @Valid UserLoginRequest request) {
		String token = loginService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(token);
	}
}
