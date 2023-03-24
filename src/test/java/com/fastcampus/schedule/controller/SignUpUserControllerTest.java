package com.fastcampus.schedule.controller;

import com.fastcampus.schedule.controller.request.SignUpUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpUserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원가입")
    @Test
    public void signUpTest() throws Exception {
        String userName = "haribo";
        String email = "aaa@mail.com";
        String password = "password";

        mvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SignUpUserRequest(userName,email,password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입시 이미 가입된 유저니름으로 회원가입 하는경우 에러 반환")
    public void existUserName_SignUp_ErrorReturn() throws Exception {
        String userName = "haribo";
        String email = "aaa@mail.com";
        String password = "password";

        mvc.perform(post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SignUpUserRequest(userName,email,password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }
}