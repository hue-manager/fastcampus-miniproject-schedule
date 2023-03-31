package com.fastcampus.schedule.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fastcampus.schedule.loginlog.service.LoginLogService;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.controller.requset.UserLoginRequest;
import com.fastcampus.schedule.user.service.LoginService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Api(tags = "로그인")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final LoginLogService loginLogService;

    @PostMapping("/login")
    public HttpEntity<String> login(@RequestBody @Valid UserLoginRequest request, HttpServletRequest httpServletRequest) {
        String token = loginService.login(request.getEmail(), request.getPassword());
        User user = userService.getUserByEmail(request.getEmail());
        String agent = httpServletRequest.getHeader("User-Agent");
        String clientIp = httpServletRequest.getRemoteAddr();
        LocalDateTime loginTime = LocalDateTime.now(); // 로그인 시간 기록
        loginLogService.createLoginLog(user, agent, clientIp, loginTime);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public HttpEntity<Void> logout() {
        loginService.logout();
        return ResponseEntity.ok(null);
    }
}
