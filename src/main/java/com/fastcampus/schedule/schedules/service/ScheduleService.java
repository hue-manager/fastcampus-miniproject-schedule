package com.fastcampus.schedule.schedules.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.repository.ScheduleRepository;
import com.fastcampus.schedule.user.User;
import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final UserRepository userRepository;

	public void save(ScheduleRequest request, String userName) {
		User user = userRepository.findByUserName(userName)
								  .orElseThrow(() -> new ScheduleException(ErrorCode.USER_NOT_FOUND, ""));

		Schedule entity = ScheduleRequest.toEntity(request, user);
		scheduleRepository.save(entity);
	}
}
