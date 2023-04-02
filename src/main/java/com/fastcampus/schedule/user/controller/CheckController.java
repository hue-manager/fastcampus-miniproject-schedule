package com.fastcampus.schedule.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class CheckController {

	private final UserService userService;

	@PostMapping("/api/check-duplicated")
	public ResponseEntity<Boolean> checkEmailDuplicated(String email) {
		userService.checkEmailDuplicated(email);
		return ResponseEntity.ok(true);
	}

}
