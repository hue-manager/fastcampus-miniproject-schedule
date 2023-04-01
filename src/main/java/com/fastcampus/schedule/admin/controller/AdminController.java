package com.fastcampus.schedule.admin.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.config.response.Response;
import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.service.ScheduleService;
import com.fastcampus.schedule.user.controller.request.UserLoginRequest;
import com.fastcampus.schedule.user.controller.response.UserResponse;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.service.LoginService;
import com.fastcampus.schedule.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "어드민")
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;
	private final LoginService loginService;
	private final ScheduleService scheduleService;

	@PostMapping("/login")
	public Response<String> login(@RequestBody @Valid UserLoginRequest request) {
		User user = userService.getUserByEmail(request.getEmail());
		if (!user.getRole().equals(Role.ROLE_ADMIN)) {
			throw new ScheduleException(ErrorCode.INVALID_ROLE, "관리자가 아닙니다.");
		}
		String token = loginService.login(request.getEmail(), request.getPassword());
		return Response.success(token);
	}

	@GetMapping("/users")
	public Response<Page<UserResponse>> users(Pageable pageable) {
		Page<UserResponse> users = userService.getUserList(Role.DEFAULT, pageable);
		return Response.success(users);
	}

	@GetMapping("/schedules")
	public Response<Page<ScheduleResponse>> schedules(Pageable pageable) {
		Page<ScheduleResponse> results = scheduleService.getSchedulesByStatus(pageable);
		return Response.success(results);
	}

	@PostMapping("/{scheduleId}/confirm")
	public Response<Void> confirmSchedule(@PathVariable Long scheduleId) {
		scheduleService.confirm(scheduleId);
		return Response.success();
	}

	@PostMapping("/{userId}/confirm")
	public HttpEntity<Void> confirmUser(@PathVariable Long userId) {
		userService.confirm(userId);
		return ResponseEntity.ok(null);
	}

}
