package com.fastcampus.schedule.schedules.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fastcampus.schedule.schedules.constant.Category;
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
						.content(mapper.writeValueAsBytes(new ScheduleRequest(Category.VACATION.name(),
								LocalDate.of(2023, 03, 23),
								LocalDate.of(2023, 03, 25),
								"memo"))))
				.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	void 유저_스케줄_일간_조회() throws Exception {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		Long userId = 1L;
		LocalDate date = LocalDate.of(2023, 4, 1);
		List<ScheduleResponse> schedules = new ArrayList<>();
		schedules.add(new ScheduleResponse(1L, Category.WORK, user, LocalDate.of(2023,4,1), LocalDate.of(2023,4,1), "test"));

		when(scheduleService.getSchedulesByDay(date, userId)).thenReturn(schedules);

		// when
		mockMvc.perform(get("/schedules/" + userId + "/day")
						.param("date", "2023-04-01")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startDate", is("2023-04-01")))
				.andExpect(jsonPath("$[0].endDate", is("2023-04-01")));

		// then
		verify(scheduleService, times(1)).getSchedulesByDay(date, userId);
	}

	@Test
	void 유저_스케줄_주간_조회() throws Exception {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		Long userId = 1L;
		LocalDate date = LocalDate.of(2023, 3, 27); // 월요일
		List<ScheduleResponse> schedules = new ArrayList<>();
		schedules.add(new ScheduleResponse(1L, Category.WORK, user, LocalDate.of(2023, 3, 27), LocalDate.of(2023, 4, 2), "test"));

		when(scheduleService.getSchedulesByWeek(date, userId)).thenReturn(schedules);

		// when
		mockMvc.perform(get("/schedules/" + userId + "/week")
						.param("date", "2023-03-27")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startDate", is("2023-03-27")))
				.andExpect(jsonPath("$[0].endDate", is("2023-04-02")));

		// then
		verify(scheduleService, times(1)).getSchedulesByWeek(date, userId);
	}

	@Test
	void 유저_스케줄_월간조회() throws Exception {
		// given
		Long userId = 1L;
		LocalDate date = LocalDate.of(2023, 3, 1);
		List<ScheduleResponse> schedules = new ArrayList<>();
		schedules.add(
				new ScheduleResponse(1L, Category.WORK, User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER),
						LocalDate.of(2023,3,1),
						LocalDate.of(2023,3,31),
						"test"));

		when(scheduleService.getSchedulesByMonth(date, userId)).thenReturn(schedules);

		// when
		mockMvc.perform(get("/schedules/" + userId + "/month")
						.param("date", "2023-03-01")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startDate", is("2023-03-01")))
				.andExpect(jsonPath("$[0].endDate", is("2023-03-31")));

		// then
		verify(scheduleService, times(1)).getSchedulesByMonth(date, userId);
	}

	@Test
	void 유저_스케줄_연간조회() throws Exception {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		Long userId = 1L;
		LocalDate date = LocalDate.of(2023, 1, 1);
		List<ScheduleResponse> schedules = new ArrayList<>();
		schedules.add(
				new ScheduleResponse(
						1L,
						Category.WORK,
						user,
						LocalDate.of(2023,4,1),
						LocalDate.of(2023,4,1),
						"test")
		);

		when(scheduleService.getSchedulesByYear(date, userId)).thenReturn(schedules);

		// when
		mockMvc.perform(get("/schedules/" + userId + "/year")
						.param("date", "2023-01-01")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startDate", is("2023-04-01")))
				.andExpect(jsonPath("$[0].endDate", is("2023-04-01")));

		// then
		verify(scheduleService, times(1)).getSchedulesByYear(date, userId);
	}
}