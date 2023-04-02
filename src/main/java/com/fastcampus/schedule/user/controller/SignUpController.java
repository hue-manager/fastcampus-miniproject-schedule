package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

	/*@PostMapping("/signup")
	public HttpEntity<String> SignUp(@RequestBody @Valid SignUpRequest request) {
		userService.signUp(request);
		return ResponseEntity.ok(SIGN_UP_SUCCESS);
	}*/
    @PostMapping("/signup")
    public HttpEntity<Void> SignUp(@RequestBody @Valid SignUpRequest request, BindingResult result) {
        if (!request.getPassword().equals(request.getPassword2())) {
            result.rejectValue("memberPassword2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
        }

        try {
            userService.signUp(request);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            result.reject("signupFailed", "이미 등록된 사용자 ID 입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            result.reject("signupFailed", e.getMessage());
        }
        return ResponseEntity.ok(null);
    }
}
