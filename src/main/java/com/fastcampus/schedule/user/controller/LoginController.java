package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.user.controller.request.UserLoginRequest;
import com.fastcampus.schedule.user.service.LoginService;

import lombok.RequiredArgsConstructor;

@Api(tags = "로그인")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public HttpEntity<String> login(@RequestBody @Valid UserLoginRequest request) {
        String token = loginService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public HttpEntity<Void> logout() {
        loginService.logout();
        return ResponseEntity.ok(null);
    }
}
