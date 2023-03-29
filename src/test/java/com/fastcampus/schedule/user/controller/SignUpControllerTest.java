package com.fastcampus.schedule.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastcampus.schedule.user.controller.requset.UserInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fastcampus.schedule.user.controller.requset.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("회원가입")
	@Test
	public void signUpTest() throws Exception {
		String userName = "haribo";
		String email = "aaa@mail.com";
		String password = "password01";

		mvc.perform(post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(new SignUpRequest(email,
																				  userName,
																				  password,
																				  "010-1111-2222")))
		   ).andDo(print())
		   .andExpect(status().isOk());
	}

	@Test
	public void userEditInfoTest() throws Exception{

		String userName = "haribo";
		String email = "aaa@mail.com";
		String password = "password01";


		mvc.perform(post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(new SignUpRequest(email,
								userName,
								password,
								"010-1111-2222"))))
				.andDo(print())
				.andExpect(status().isOk());

		String email2 = "bbb@gmail.com";
		String userName2 = "test";

		mvc.perform(post("/users/1/editinfo")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsBytes(new UserInfoRequest(email2, userName2)))
						// email과 username 둘 다 수정할경우는 status 200 뜸
				).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void userEditInfoTest2() throws Exception{

		String userName = "haribo";
		String email = "aaa@mail.com";
		String password = "password01";


		mvc.perform(post("/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(new SignUpRequest(email,
								userName,
								password,
								"010-1111-2222"))))
				.andDo(print())
				.andExpect(status().isOk());

		String email2 = "bbb@gmail.com";
		String userName2 = "test";

		mvc.perform(post("/users/1/editinfo")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsBytes(new UserInfoRequest(email2)))
						// email과 username중 하나만 수정할경우는 status 400 뜸
						// -> 하나만 받아도 되도록 UserInfoRequest 에서 생성자 추가했지만 왜 실패인지 모르겠음...
				).andDo(print())
				.andExpect(status().isOk());
	}
}