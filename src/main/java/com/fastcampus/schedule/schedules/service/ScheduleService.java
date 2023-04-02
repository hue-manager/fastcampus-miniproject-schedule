package com.fastcampus.schedule.schedules.service;

import static java.util.stream.Collectors.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.fastcampus.schedule.user.domain.constant.Role;
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

	public Page<ScheduleResponse> findAll(Pageable pageable, Status permit) {
		return scheduleRepository.findAllByStatus(pageable, permit).map(ScheduleResponse::fromEntity);
	}

	public Schedule save(ScheduleRequest request, String email) {

		User user = loadUserByEmail(email);

		if (getRemainVacationCount(request, user) < 0) {
			throw new ScheduleException(ErrorCode.NOT_ENOUGH_COUNT, "연차가 부족합니다.");
		}
		// 연차 삭감
		cutVacationCount(request, user);

		Schedule entity = ScheduleRequest.toEntity(request, user);
		scheduleRepository.save(entity);
		return entity;
	}

	private User loadUserByEmail(String email) {
		return userRepository.findByEmail(email)
							 .orElseThrow(() -> new ScheduleException(ErrorCode.USER_NOT_FOUND, ""));
	}

	public ScheduleResponse edit(Long scheduleId, ScheduleRequest request, String email) {

		User user = loadUserByEmail(email);
		Schedule schedule = getScheduleOrException(scheduleId);

		//유저와 일정을 쓴 유저와 맞는지 확인
		if (!schedule.getUser().equals(user)) {
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email));
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

		updateSchedule(schedule, request);
		return ScheduleResponse.fromEntity(schedule);
	}


	public void delete(String email, Long scheduleId) {

		User user = loadUserByEmail(email);
		Schedule schedule = getScheduleOrException(scheduleId);

		// 연차 복구
		if (schedule.getCategory().equals(Category.VACATION)) {
			user.setVacationCount(
				user.getVacationCount() + (schedule.getEndDate().compareTo(schedule.getStartDate()) + 1));
		}

		if (!schedule.getUser().equals(user)) { // 작성자와 유저정보와 같은지 확인
			throw new ScheduleException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email));
		}
		//작성자로 조회후 삭제
		schedule.deleteUser();
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

	public ByteArrayInputStream createExcelFile(List<Schedule> schedules) {
		try (Workbook workbook = new XSSFWorkbook();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Schedules");

			// 헤더 생성
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("User");
			headerRow.createCell(1).setCellValue("Category");
			headerRow.createCell(2).setCellValue("Start Date");
			headerRow.createCell(3).setCellValue("End Date");
			headerRow.createCell(4).setCellValue("Status");
			headerRow.createCell(5).setCellValue("Memo");

			// 데이터 추가
			for (int i = 0; i < schedules.size(); i++) {
				Schedule schedule = schedules.get(i);
				Row row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(schedule.getUser().getUsername());
				row.createCell(1).setCellValue(schedule.getCategory().toString());
				row.createCell(2).setCellValue(schedule.getStartDate().toString());
				row.createCell(3).setCellValue(schedule.getEndDate().toString());
				row.createCell(4).setCellValue(schedule.getStatus().toString());
				row.createCell(5).setCellValue(schedule.getMemo());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Excel 파일 생성 실패", e);
		}
	}

	public List<Schedule> getSchedulesByRole(Role role, Long userId) {
		if (Role.ROLE_ADMIN.equals(role)) {
			// 관리자의 경우 모든 스케줄을 가져옴
			return scheduleRepository.findAll();
		} else {
			// 일반 유저의 경우 해당 유저의 스케줄만 가져옴
			return scheduleRepository.findAllByUser_Id(userId);
		}
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

	public Page<ScheduleResponse> getSchedulesByStatus(Status status, Pageable pageable) {
		return scheduleRepository.findAllByStatus(pageable, status)
								 .map(ScheduleResponse::fromEntity);
	}

	public void confirm(Long scheduleId) {
		Schedule schedule = getScheduleOrException(scheduleId);
		if (schedule.getStatus().equals(Status.WAITING)) {
			schedule.setStatus(Status.PERMIT);
		} else {
			throw new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND, "대기 중이 아닙니다.");
		}
	}

	public Page<ScheduleResponse> findAllByUserId(Long userId, Pageable pageable) {
		return scheduleRepository.findAllByUserId(userId, pageable)
								 .map(ScheduleResponse::fromEntity);
	}

	public void reject(Long scheduleId) {
		Schedule schedule = getScheduleOrException(scheduleId);
		if (schedule.getStatus().equals(Status.WAITING)) {
			delete(schedule.getUser().getEmail(), scheduleId);
		} else {
			throw new ScheduleException(ErrorCode.SCHEDULE_NOT_FOUND, "대기 중이 아닙니다.");
		}
	}
	private void updateSchedule(Schedule schedule, ScheduleRequest request) {
		schedule.setStatus(Status.WAITING);
		schedule.setCategory(Category.valueOf(request.getCategory()));
		schedule.setMemo(request.getMemo());
		schedule.setStartDate(request.getStartDate());
		schedule.setEndDate(request.getEndDate());
	}
}
