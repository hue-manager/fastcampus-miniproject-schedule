package com.fastcampus.schedule.schedules.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Status;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	Page<Schedule> findAllByUserId(Long userId, Pageable pageable);

	List<Schedule> findAllByUser_Id(Long userId);

	Page<Schedule> findAll(Pageable pageable);
	Page<Schedule> findAllByStatus(Pageable pageable, Status status);

	// 시작 날짜와 종료 날짜가 주어진 범위 내에 있는 경우 조회
	List<Schedule> findByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long userId, LocalDate startDate, LocalDate endDate);
	// 시작 날짜가 주어진 범위 내에 있는 경우를 조회
	List<Schedule> findByUser_IdAndStartDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
	// 시작 날짜와 종료 날짜가 모두 주어진 범위 내에 있는 경우를 조회
	List<Schedule> findByUser_IdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Long userId, LocalDate startDate, LocalDate endDate);
	// 시작 날짜가 주어진 범위 내에 있거나 종료 날짜가 주어진 범위 내에 있는 경우 조회
	List<Schedule> findByUser_IdAndStartDateBetweenOrUser_IdAndEndDateBetween(Long userId, LocalDate startYearRange, LocalDate endYearRange, Long userId2, LocalDate startYearRange2, LocalDate endYearRange2);
}
