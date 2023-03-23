package com.fastcampus.schedule.schedules.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public Schedule findById(Long scheduleId) {
		return getScheduleOrException(scheduleId);
	}

	public Page<Schedule> findAllByUserId(Long userId, Pageable pageable) {
		return scheduleRepository.findAllByUserId(userId, pageable);
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

	//유저 확인 로직(중복제거위해 메소드로 뺐음)
	private User getUserOrException(String userName) {
		return userRepository.findByUserName(userName)
							 .orElseThrow(() -> new ScheduleException(ErrorCode.USER_NOT_FOUND, ""));
	}

	//등록일정 확인 로직(중복제거위해 메소드로 뺐음)
	private Schedule getScheduleOrException(Long scheduleId) {
		return scheduleRepository.findById(scheduleId)
								 .orElseThrow(() -> new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND, ""));
	}
}
