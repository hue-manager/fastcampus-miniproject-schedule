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

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	Page<Schedule> findAllByUserId(Long userId, Pageable pageable);

	List<Schedule> findAllByUser_Id(Long userId);

	@Query("SELECT s FROM Schedule s WHERE s.user.id = :userId AND s.startDate BETWEEN :startDate AND :endDate AND s.endDate BETWEEN :startDate AND :endDate")
	List<Schedule> findSchedulesByUserAndPeriod(
			@Param("userId") Long userId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}
