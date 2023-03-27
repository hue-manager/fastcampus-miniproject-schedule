package com.fastcampus.schedule.schedules.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Status;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	Page<Schedule> findAllByUserId(Long userId, Pageable pageable);

	List<Schedule> findAllByUser_Id(Long userId);

	Page<Schedule> findAllByStatus(Pageable pageable, Status status);
}
