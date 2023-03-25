package com.fastcampus.schedule.schedules.service;

import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

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
}
