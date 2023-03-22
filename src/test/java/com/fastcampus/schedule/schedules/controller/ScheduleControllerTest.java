package com.fastcampus.schedule.schedules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fastcampus.schedule.schedules.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	ScheduleService scheduleService;
	@Autowired
	ObjectMapper mapper;
}
