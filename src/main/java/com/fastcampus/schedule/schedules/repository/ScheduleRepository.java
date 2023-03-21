package com.fastcampus.schedule.schedules.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.schedule.schedules.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
