package com.fastcampus.schedule.controller;

import com.fastcampus.schedule.controller.request.SignUpUserRequest;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.UserDto;
import com.fastcampus.schedule.user.domain.UserResponse;
import com.fastcampus.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SignUpUserController {

    private final UserService userService;

//    @GetMapping("/signUp")
//    public String signup() {
//        return "signup_form";
//    }

    @PostMapping("/signUp")
    public void SignUp(@RequestBody User user) throws Exception {
        userService.signUp(user);
    }

}
