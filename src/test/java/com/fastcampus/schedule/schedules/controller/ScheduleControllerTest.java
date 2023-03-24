package com.fastcampus.schedule.schedules.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	ScheduleService scheduleService;
	@Autowired
	ObjectMapper mapper;

	@Test
	@WithMockUser
	void 연차_등록() throws Exception {
		mockMvc.perform(post("/schedules/save")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsBytes(new ScheduleRequest("WORK",
															  LocalDate.of(2023, 03, 23),
															  LocalDate.of(2023, 03, 25),
															  "memo"))))
			   .andDo(print())
			.andExpect(status().isOk());
	}
}
