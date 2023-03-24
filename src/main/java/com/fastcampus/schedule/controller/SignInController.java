package com.fastcampus.schedule.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fastcampus.schedule.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SignInController {

	//@Autowired UserRepository userRepository;
	private final UserService userService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/loginForm")
	public String loginForm() {
		return "/loginForm";
	}

	//    @PostMapping("/signUn")
	//    public String signIn(UserDto user){
	//        //user.setRole(ROLE);
	//        user.setRole(Role.ROLE_ADMIN);
	//        //userDto에 입력된 password를 가져와서 rawPassword에 담아주자
	//        String rawPassword = userService.getUser();
	//        //bCryptpasswordEncoder.encode(rawPassword)로 변환해서 encPassword에 넣어주자
	//        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
	//        //userDto에 setPassword(encPassword)호출
	//        user.setPassword(encPassword);
	//        //userRepository에 save(userDto)
	//        userService.save(user);
	//        return "redirect:/loginForm";
	//    }
}
