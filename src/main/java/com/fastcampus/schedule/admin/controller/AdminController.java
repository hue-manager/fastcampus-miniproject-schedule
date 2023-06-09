package com.fastcampus.schedule.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.config.jwt.JwtUtils;
import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.schedules.constant.Status;
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

	public static final String LOGIN_SUCCESS = "관리자 로그인 성공";
	public static final String SUCCESS = "성공";
	private final UserService userService;
	private final LoginService loginService;
	private final ScheduleService scheduleService;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@PostMapping("/login")
	public HttpEntity<Map> login(@RequestBody @Valid UserLoginRequest request,
								 HttpServletRequest httpServletRequest) {

		Map<String, String> map = new HashMap<>();

		User user = userService.getUserByEmail(request.getEmail());
		if (!user.getRole().equals(Role.ROLE_ADMIN)) {
			throw new ScheduleException(ErrorCode.INVALID_ROLE, "관리자가 아닙니다.");
		}
		//		if (httpServletRequest.getHeader("token") != null) {
		//			map.put("message", "이미 관리자 로그인 되어있습니다.");
		//			return ResponseEntity.status(400).body(map);
		//		}

		String token = loginService.login(request.getEmail(), request.getPassword());
		map.put("userId", loginService.getUserIdByEmail(request.getEmail()));
		map.put("token", token);
		map.put("message", LOGIN_SUCCESS);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/users")
	public HttpEntity<Page<UserResponse>> users(
		@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
		HttpServletRequest request) {

		checkRoleAdmin(request);

		Page<UserResponse> users = userService.getUserList(Role.DEFAULT, pageable);
		if (users.isEmpty()) {
			return ResponseEntity.ok()
								 .header("X-Message", "No users found")
								 .body(Page.empty());
		}
		return ResponseEntity.ok(users);

	}

	@GetMapping("/schedules")
	public ResponseEntity<Page<ScheduleResponse>> schedules(
		@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
		HttpServletRequest request) {

		checkRoleAdmin(request);

		Page<ScheduleResponse> results = scheduleService.getSchedulesByStatus(Status.WAITING, pageable);
		if (results.isEmpty()) {
			return ResponseEntity.ok()
								 .header("X-Message", "No schedules found")
								 .body(Page.empty());
		}
		return ResponseEntity.ok()
							 .header("X-Message", "Schedules retrieved successfully")
							 .body(results);
	}

	@PostMapping("/{scheduleId}/confirm-schedule")
	public HttpEntity<String> confirmSchedule(@PathVariable Long scheduleId,
											  HttpServletRequest request) {

		checkRoleAdmin(request);

		scheduleService.confirm(scheduleId);
		return ResponseEntity.ok(SUCCESS);
	}

	@PostMapping("/{userId}/confirm-user")
	public HttpEntity<String> confirmUser(@PathVariable Long userId,
										  HttpServletRequest request) {

		checkRoleAdmin(request);
		userService.confirm(userId);
		return ResponseEntity.ok(SUCCESS);
	}

	@PostMapping("/{scheduleId}/reject-schedule")
	public HttpEntity<String> rejectSchedule(@PathVariable Long scheduleId,
											 HttpServletRequest request) {

		checkRoleAdmin(request);

		scheduleService.reject(scheduleId);
		return ResponseEntity.ok(SUCCESS);
	}

	private void checkRoleAdmin(HttpServletRequest request) {
		String email = getEmailByToken(request);
		User user = userService.getUserByEmail(email);

		if (!user.getRole().equals(Role.ROLE_ADMIN)) {
			throw new ScheduleException(ErrorCode.INVALID_ROLE, "관리자가 아닙니다.");
		}
	}

	private String getEmailByToken(HttpServletRequest ServletRequest) {
		String token = ServletRequest.getHeader("Authorization").split(" ")[1].trim();
		String email = JwtUtils.getEmail(token, secretKey);
		return email;
	}
}
