package com.fastcampus.schedule.schedules.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

	private final ScheduleService scheduleService;

	@GetMapping("/{scheduleId}")
	public HttpEntity<ScheduleResponse> getInfo(@PathVariable Long scheduleId) {
	}

	@GetMapping("/schedules/{userId}")
	public HttpEntity<Page<ScheduleResponse>> getList(@PathVariable Long userId) {
	}

	@PostMapping("/save")  //저장
	public HttpEntity<Void> save(@RequestBody @Valid ScheduleRequest request,
								 Authentication authentication) {
		scheduleService.save(request, authentication.getName());
		return ResponseEntity.ok(null);
	}

	@PostMapping("/{scheduleId}/edit")  //수정
	public ResponseEntity<ScheduleResponse> edit(@PathVariable Long scheduleId,
												 @RequestBody @Valid ScheduleRequest request,
												 Authentication authentication) {

		Schedule schedule = scheduleService.edit(scheduleId, request, authentication.getName());

		return ResponseEntity.ok().body(ScheduleResponse.fromEntity(schedule));  // 수정하고 ScheduleResponse 반환
	}

	@PostMapping("/{scheduleId}/delete") //삭제
	public ResponseEntity<Void> delete(@PathVariable Long scheduleId,
									   Authentication authentication) {
		scheduleService.delete(authentication.getName(), scheduleId);

		return ResponseEntity.ok().build();
	}
}
