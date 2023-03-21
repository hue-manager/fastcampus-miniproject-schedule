package com.fastcampus.schedule.schedules.service;

import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.repository.ScheduleRepository;
import com.fastcampus.schedule.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;


    public List<ScheduleResponse> getSchedulesByDay (LocalDate date, Authentication authentication){
        Long userId = ((User) authentication.getPrincipal()).getId();

        return scheduleRepository
                .findAllByUserId(userId) // 유저 전체 조회
                .stream()
                .filter(schedule -> schedule.isOverlapped(date)) //입력된 날짜로 조회
                .map(schedule -> ScheduleResponse.fromEntity(schedule))
                .collect(Collectors.toList());
    }
}

