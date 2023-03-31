package com.fastcampus.schedule.user.controller;

import com.fastcampus.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CheckController {

    private final UserService userService;

    @PostMapping("/api/check-duplicated")
    public ResponseEntity<Boolean> checkEmailDuplicated(String email){
        userService.checkEmailDuplicated(email);
        return ResponseEntity.ok(true);
    }

}
