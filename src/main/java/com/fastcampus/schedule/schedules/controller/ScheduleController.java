package com.fastcampus.schedule.schedules.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

	private final ScheduleService scheduleService;

	@PostMapping("/save")
	public HttpEntity<Void> save(ScheduleRequest request, Authentication authentication) {
		scheduleService.save(request, authentication.getName());
		return ResponseEntity.ok(null);
	}
}
