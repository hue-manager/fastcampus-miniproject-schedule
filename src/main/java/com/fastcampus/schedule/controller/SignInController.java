package com.fastcampus.schedule.controller;

import com.fastcampus.schedule.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class SignInController {

    //@Autowired UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/loginForm";
    }

    @GetMapping("signInForm")
    public String signInForm(){
        return "/signInForm";
    }
    @PostMapping("/signIn")
    public String signIn(User user){
        //user.setRole(ROLE);
        //userDto에 입력된 password를 가져와서 rawPassword에 담아주자

        //bCryptpasswordEncoder.encode(rawPassword)로 변환해서 encPassword에 넣어주자

        //userDto에 setPassword(encPassword)호출

        //userRepository에 save(userDto)
        return "redirect:/loginForm";
    }
}
