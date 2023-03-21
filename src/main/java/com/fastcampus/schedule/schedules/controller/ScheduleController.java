package com.fastcampus.schedule.schedules.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.service.ScheduleQueryService;
import com.fastcampus.schedule.schedules.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final ScheduleQueryService scheduleQueryService;

	@PostMapping("/save")  //저장
	public HttpEntity<Void> save(@RequestBody @Valid ScheduleRequest request,
								 Authentication authentication) {
		scheduleService.save(request, authentication.getName());
		return ResponseEntity.ok(null);
	}

	@PostMapping("/edit/{scheduleId}")  //수정
	public ResponseEntity<ScheduleResponse> edit(@PathVariable Long scheduleId,
												 @RequestBody @Valid ScheduleRequest request,
												 Authentication authentication) {

		Schedule schedule = scheduleService.edit(scheduleId, request, authentication.getName());

		return ResponseEntity.ok().body(ScheduleResponse.fromEntity(schedule));  // 수정하고 ScheduleResponse 반환
	}

	@PostMapping("/delete/{scheduleId}") //삭제
	public ResponseEntity<Void> delete(@PathVariable Long scheduleId,
									   Authentication authentication) {
		scheduleService.delete(authentication.getName(), scheduleId);

		return ResponseEntity.ok().build();
	}

	//일 조회
	@GetMapping("/schedules/{userId}")
	public List<ScheduleResponse> getSchedulesByDay(Authentication authentication,
													@RequestParam(required = false)
													@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		return scheduleQueryService.getSchedulesByDay(date == null ? LocalDate.now() : date, authentication);
	}

}
