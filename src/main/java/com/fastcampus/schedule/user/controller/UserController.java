package com.fastcampus.schedule.user.controller;

import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.UserRequest;
import com.fastcampus.schedule.user.domain.UserResponse;
import com.fastcampus.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // 유저 권한 수정
    @PostMapping("/{userId}/editrole")
    public ResponseEntity<Void> editUserRole(@PathVariable Long userId, @RequestBody UserRequest request){
        userService.editUserRole(userId, request);
        return ResponseEntity.ok(null);
    }

    // 유저 정보 수정
    @PostMapping("/{userId}/editinfo")
    public ResponseEntity<Void> editUserInfo(@PathVariable Long userId, @RequestBody UserRequest request){
        userService.editUserInfo(userId, request);
        return ResponseEntity.ok(null);
    }

    //유저 전체 정보 조회
    @GetMapping("/")
    public ResponseEntity<ModelMap> getUserList(ModelMap modelMap, @RequestParam(required = false) Role role, Pageable pageable){
        Page<UserResponse> userList = userService.getUserList(role, pageable);
        modelMap.addAttribute("Users", userList);
        return ResponseEntity.ok().body(modelMap);
    }

    // 유저 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ModelMap> getUser(@PathVariable Long userId, ModelMap modelMap){
        UserResponse user = userService.getUser(userId);
        modelMap.addAttribute("User", user);
        return ResponseEntity.ok().body(modelMap);
    }


}
