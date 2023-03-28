package com.fastcampus.schedule.schedules.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.util.Period;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.repository.ScheduleRepository;
import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

	@InjectMocks
	ScheduleService scheduleService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	@Test
	void 연차_삭감() {
		User user = User.of("eggwe@gmail.com",
							"sssss",
							"password00",
							"010-1111-2222",
							Role.ROLE_USER);

		ScheduleRequest request = new ScheduleRequest("VACATION",
													  LocalDate.of(2023, 03, 23),
													  LocalDate.of(2023, 03, 25),
													  "");

		Mockito.when(userRepository.findByUserName("123")).thenReturn(Optional.of(user));
		Schedule saved = scheduleService.save(request, "123");

		Assertions.assertThat(saved.getUser().getVacationCount()).isEqualTo(12);

	}

	@Test
	void 유저_스케줄_일간조회() {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		Long userId = 1L;
		LocalDate date = LocalDate.of(2023, 3, 28);
		Period period = Period.of(date, date);

		List<Schedule> schedules = new ArrayList<>();
		schedules.add(Schedule.of(user, Category.WORK, LocalDate.of(2023,3,28),LocalDate.of(2023,3,28),"test1"));
		schedules.add(Schedule.of(user, Category.WORK, LocalDate.of(2023,3,28),LocalDate.of(2023,3,28),"test2"));

		when(scheduleRepository.findSchedulesByUserAndPeriod(userId, period.getStartDate(), period.getEndDate())).thenReturn(schedules);

		// when
		List<ScheduleResponse> scheduleResponses = scheduleService.getSchedulesByDay(date, userId);

		// then
		assertEquals(2, scheduleResponses.size());
		assertEquals("test1", scheduleResponses.get(0).getMemo());
		assertEquals("test2", scheduleResponses.get(1).getMemo());

	}

	@Test
	void 유저_스케줄_주간조회() {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		LocalDate startOfWeek = LocalDate.of(2023, 3, 27); // 2023년 3월 27일 (월요일)
		Long userId = 1L;
		Long scheduleId = 3L;
		Period period = Period.of(startOfWeek, startOfWeek.plusDays(6));

		List<Schedule> schedules = new ArrayList<>();
		schedules.add(Schedule.of(user, Category.WORK, LocalDate.of(2023,3,27),LocalDate.of(2023,4,2),"weekTest"));


		when(scheduleRepository.findSchedulesByUserAndPeriod(userId, period.getStartDate(), period.getEndDate())).thenReturn(schedules);



		// when
		List<ScheduleResponse> schedulesResponse = scheduleService.getSchedulesByWeek(startOfWeek, userId);

		// then
		assertNotNull(schedulesResponse);
		assertEquals(startOfWeek, schedulesResponse.get(0).getStartDate());
		assertEquals(startOfWeek.plusDays(6), schedulesResponse.get(0).getEndDate());
		assertEquals("weekTest", schedulesResponse.get(0).getMemo());

	}

	@Test
	void 유저_스케줄_주간조회_잘못된_시작날짜() {
		// given

		LocalDate startOfWeek = LocalDate.of(2023, 3, 28); // 2023년 3월 28일 (화요일)
		Long userId = 1L;

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			scheduleService.getSchedulesByWeek(startOfWeek, userId);
		});
	}

	@Test
	void 유저_스케줄_월간조회() {
		// given
		User user = User.of("asdf@gmail.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		LocalDate date = LocalDate.of(2023, 4, 15);
		Long userId = 1L;
		Period period = Period.of(date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()));

		List<Schedule> schedules = new ArrayList<>();
		schedules.add(Schedule.of(user, Category.WORK, LocalDate.of(2023,4,1),LocalDate.of(2023,4,30),"test1"));

		when(scheduleRepository.findSchedulesByUserAndPeriod(userId, period.getStartDate(), period.getEndDate()))
				.thenReturn(schedules);

		// when
		List<ScheduleResponse> schedulesResponse = scheduleService.getSchedulesByMonth(date, userId);

		// then
		assertNotNull(schedulesResponse);
		assertEquals(date.withDayOfMonth(1), schedulesResponse.get(0).getStartDate());
		assertEquals(date.withDayOfMonth(date.lengthOfMonth()), schedulesResponse.get(0).getEndDate());
		assertEquals(Category.WORK, schedulesResponse.get(0).getCategory());
	}

	@Test
	void 유저_스케줄_연간조회() {
		// given
		User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);
		Long userId = 1L;

		// 2023년 스케줄 생성
		List<Schedule> schedules2023 = new ArrayList<>();
		schedules2023.add(Schedule.of(user, Category.WORK, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 7), "test1"));
		schedules2023.add(Schedule.of(user, Category.WORK, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 7), "test2"));
		schedules2023.add(Schedule.of(user, Category.WORK, LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 31), "test3"));

		// 2024년 스케줄 생성
		List<Schedule> schedules2024 = new ArrayList<>();
		schedules2024.add(Schedule.of(user, Category.WORK, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7), "test4"));
		schedules2024.add(Schedule.of(user, Category.WORK, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 7), "test5"));

		// Repository에서 반환될 스케줄 리스트 설정
		List<Schedule> schedules = new ArrayList<>();
		schedules.addAll(schedules2023);
		schedules.addAll(schedules2024);
		when(scheduleRepository.findSchedulesByUserAndPeriod(userId, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)))
				.thenReturn(schedules2023);
		when(scheduleRepository.findSchedulesByUserAndPeriod(userId, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)))
				.thenReturn(schedules2024);

		// when
		List<ScheduleResponse> schedulesResponse2023 = scheduleService.getSchedulesByYear(LocalDate.of(2023, 5, 1), userId);
		List<ScheduleResponse> schedulesResponse2024 = scheduleService.getSchedulesByYear(LocalDate.of(2024, 5, 1), userId);

		// then
		assertNotNull(schedulesResponse2023);
		assertEquals(3, schedulesResponse2023.size());
		assertEquals(schedules2023.get(0).getMemo(), schedulesResponse2023.get(0).getMemo());
		assertEquals(schedules2023.get(1).getMemo(), schedulesResponse2023.get(1).getMemo());
		assertEquals(schedules2023.get(2).getMemo(), schedulesResponse2023.get(2).getMemo());

		assertNotNull(schedulesResponse2024);
		assertEquals(2, schedulesResponse2024.size());
		assertEquals(schedules2024.get(0).getMemo(), schedulesResponse2024.get(0).getMemo());
		assertEquals(schedules2024.get(1).getMemo(), schedulesResponse2024.get(1).getMemo());
	}

}