package com.fastcampus.schedule.schedules.service;

import static java.util.stream.Collectors.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.schedules.constant.Status;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.repository.ScheduleRepository;
import com.fastcampus.schedule.schedules.util.Period;
import com.fastcampus.schedule.user.domain.User;
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

	public Schedule save(ScheduleRequest request, String userName) {

		User user = getUserOrException(userName);

		if (getRemainVacationCount(request, user) < 0) {
			throw new ScheduleException(ErrorCode.NOT_ENOUGH_COUNT, "연차가 부족합니다.");
		}
		// 연차 삭감
		cutVacationCount(request, user);

		Schedule entity = ScheduleRequest.toEntity(request, user);
		scheduleRepository.save(entity);
		return entity;
	}

	public Schedule edit(Long scheduleId, ScheduleRequest request, String userName) {

		User user = getUserOrException(userName);
		Schedule schedule = getScheduleOrException(scheduleId);

		//유저와 일정을 쓴 유저와 맞는지 확인
		if (!schedule.getUser().equals(user)) {
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName));
		}

		// 연차 원상복구
		if (schedule.getCategory().equals(Category.VACATION)) {
			user.setVacationCount(
				user.getVacationCount() + (schedule.getEndDate().compareTo(schedule.getStartDate()) + 1));
		}

		if (getRemainVacationCount(request, user) < 0) {
			throw new ScheduleException(ErrorCode.NOT_ENOUGH_COUNT, "연차가 부족합니다.");
		}
		// 연차 삭감
		cutVacationCount(request, user);

		Schedule entity = ScheduleRequest.toEntity(request, user);
		//변경 후 저장
		return scheduleRepository.saveAndFlush(schedule);
	}

	public void delete(String userName, Long scheduleId) {

		User user = getUserOrException(userName);
		Schedule schedule = getScheduleOrException(scheduleId);

		// 연차 복구
		if (schedule.getCategory().equals(Category.VACATION)) {
			user.setVacationCount(
				user.getVacationCount() + (schedule.getEndDate().compareTo(schedule.getStartDate()) + 1));
		}

		if (!schedule.getUser().equals(user)) { // 작성자와 유저정보와 같은지 확인
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName));
		}
		//작성자로 조회후 삭제
		scheduleRepository.deleteById(scheduleId);

	}

	public List<ScheduleResponse> getSchedulesByDay(LocalDate date, Long userId) {
		final Period period = Period.of(date, date);
		return getSchedulesByPeriod(userId, period);
	}

	public List<ScheduleResponse> getSchedulesByWeek(LocalDate startOfWeek, Long userId) {
		if (startOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
			throw new IllegalArgumentException("startOfWeek must be a Monday");
		}
		final Period period = Period.of(startOfWeek, startOfWeek.plusDays(6));
		return getSchedulesByPeriod(userId, period);
	}

	public List<ScheduleResponse> getSchedulesByMonth(LocalDate date, Long userId) {
		LocalDate firstDayOfMonth = date.withDayOfMonth(1);
		LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
		Period period = Period.of(firstDayOfMonth, lastDayOfMonth);

		return getSchedulesByPeriod(userId, period);
	}

	public List<ScheduleResponse> getSchedulesByYear(LocalDate date, Long userId) {
		int year = date.getYear();
		LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
		LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
		Period period = Period.of(firstDayOfYear, lastDayOfYear);

		return getSchedulesByPeriod(userId, period);
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


	private List<ScheduleResponse> getSchedulesByPeriod(Long userId, Period period) {
		List<Schedule> schedules = scheduleRepository
				.findSchedulesByUserAndPeriod(
						userId,
						period.getStartDate(),
						period.getEndDate());

		return schedules.stream()
				.map(ScheduleResponse::fromEntity)
				.collect(toList());
	}

	private static int getRemainVacationCount(ScheduleRequest request, User user) {
		return user.getVacationCount() - (request.getEndDate().compareTo(request.getStartDate()) + 1);
	}

	private static void cutVacationCount(ScheduleRequest request, User user) {
		if (request.getCategory().equals(Category.VACATION.name())) {
			user.setVacationCount(getRemainVacationCount(request, user));
		}
	}

	public Page<ScheduleResponse> getSchedulesByStatus(Pageable pageable) {
		return scheduleRepository.findAllByStatus(pageable, Status.WAITING)
								 .map(ScheduleResponse::fromEntity);
	}

	public void confirm(Long scheduleId) {
		Schedule schedule = getScheduleOrException(scheduleId);
		if (schedule.getStatus().equals(Status.WAITING)) {
			schedule.setStatus(Status.PERMIT);
		} else {
			throw new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND, "");
		}
	}
}
