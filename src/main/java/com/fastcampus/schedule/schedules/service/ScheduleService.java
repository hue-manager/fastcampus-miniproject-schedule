package com.fastcampus.schedule.schedules.service;

import javax.persistence.EntityNotFoundException;

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

	//유저 확인 로직(중복제거위해 메소드로 뺐음)
	private User getUserOrException(String userName) {
		return userRepository.findByUserName(userName).orElseThrow(() ->
																	   new EntityNotFoundException("없는 계정 입니다."));
	}

	//등록일정 확인 로직(중복제거위해 메소드로 뺐음)
	private Schedule getScheduleOrException(Long scheduleId) {
		return scheduleRepository.findById(scheduleId).orElseThrow(() ->
																	   new EntityNotFoundException("등록한 일정이 없습니다."));

	}

	public void save(ScheduleRequest request, String userName) {

		User user = getUserOrException(userName);

		Schedule entity = ScheduleRequest.toEntity(request, user);
		scheduleRepository.save(entity);
	}

	public Schedule edit(Long scheduleId, ScheduleRequest request, String userName) {

		User user = getUserOrException(userName);

		Schedule schedule = getScheduleOrException(scheduleId);

		//유저와 일정을 쓴 유저와 맞는지 확인
		if (schedule.getUser() != user) {
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName));

		}

		//변경 후 저장
		return scheduleRepository.saveAndFlush(schedule);

	}

	public void delete(String userName, Long scheduleId) {

		User user = getUserOrException(userName);

		Schedule schedule = getScheduleOrException(scheduleId);

		if (schedule.getUser() != user) { // 작성자와 유저정보와 같은지 확인
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName));

		}
		//작성자로 조회후 삭제
		scheduleRepository.deleteById(scheduleId);

	}
}