package com.fastcampus.schedule.user.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.config.response.Response;
import com.fastcampus.schedule.loginlog.service.LoginLogService;
import com.fastcampus.schedule.user.controller.request.UserLoginRequest;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.service.LoginService;
import com.fastcampus.schedule.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "로그인")
@RestController
@RequiredArgsConstructor
public class LoginController {

	public static final String LOGIN_SUCCESS = "로그인 성공";
	private final LoginService loginService;
	private final UserService userService;
	private final LoginLogService loginLogService;

	@PostMapping("/login")
	public Response<?> login(@RequestBody @Valid UserLoginRequest request,
							   HttpServletRequest httpServletRequest) {

		Map<String, String> map = new HashMap<>();

		if (httpServletRequest.getHeader("token") != null) {
			map.put("message", "이미 로그인 되어있습니다.");
			return Response.error("이미 로그인 되어있습니다.");
		}

		String token = loginService.login(request.getEmail(), request.getPassword());
		User user = userService.getUserByEmail(request.getEmail());
		map.put("token", token);
		map.put("message", LOGIN_SUCCESS);
		String agent = httpServletRequest.getHeader("User-Agent");
		String clientIp = httpServletRequest.getRemoteAddr();
		LocalDateTime loginTime = LocalDateTime.now(); // 로그인 시간 기록
		loginLogService.createLoginLog(user, agent, clientIp, loginTime);
		return Response.success(map);
	}

	@PostMapping("/logout")
	public Response<Void> logout() {
		loginService.logout();
		return Response.success();
	}
}
