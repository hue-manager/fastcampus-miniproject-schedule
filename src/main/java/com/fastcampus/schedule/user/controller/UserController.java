package com.fastcampus.schedule.user.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.config.response.Response;
import com.fastcampus.schedule.user.controller.request.DeleteAccountRequest;
import com.fastcampus.schedule.user.controller.request.UserInfoRequest;
import com.fastcampus.schedule.user.controller.request.UserRoleRequest;
import com.fastcampus.schedule.user.controller.response.UserResponse;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "유저")
@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	//회원탈퇴

	@PostMapping("/{userId}/deleteAccount")
	public Response<String> deleteAccount(@PathVariable Long userId,
										  @Valid @RequestBody DeleteAccountRequest request) {
		userService.deleteAccount(userId, request.getPassword());
		return Response.success("회원탈퇴가 완료되었습니다.");
	}

	// 유저 권한 수정
	@PostMapping("/{userId}/editrole")
	public Response<Void> editUserRole(@PathVariable Long userId, @Valid @RequestBody UserRoleRequest request) {
		userService.editUserRole(userId, request);
		return Response.success();
	}

	// 유저 정보 수정
	@PostMapping("/{userId}/editinfo")
	public Response<Void> editUserInfo(@PathVariable Long userId, @Valid @RequestBody UserInfoRequest request) {
		userService.editUserInfo(userId, request);
		return Response.success();
	}

	//유저 전체 정보 조회
	@GetMapping("/")
	public Response<ModelMap> getUserList(ModelMap modelMap, @RequestParam(required = false) Role role,
												Pageable pageable) {
		Page<UserResponse> userList = userService.getUserList(role, pageable);
		modelMap.addAttribute("users", userList);
		return Response.success(modelMap);
	}

	// 유저 정보 조회
	@GetMapping("/{userId}")
	public Response<ModelMap> getUser(@PathVariable Long userId, ModelMap modelMap) {
		UserResponse user = userService.getUser(userId);
		modelMap.addAttribute("user", user);
		return Response.success(modelMap);
	}

}
