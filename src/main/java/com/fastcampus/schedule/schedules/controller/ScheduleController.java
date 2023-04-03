package com.fastcampus.schedule.schedules.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.schedule.config.jwt.JwtUtils;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Status;
import com.fastcampus.schedule.schedules.controller.request.ScheduleRequest;
import com.fastcampus.schedule.schedules.controller.response.ScheduleResponse;
import com.fastcampus.schedule.schedules.service.ScheduleService;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.service.UserService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "스케쥴")
@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {
	private final UserService userService;
	private final ScheduleService scheduleService;
	@Value("${jwt.secret-key}")
	private String secretKey;

	@GetMapping("/scheduleinfo/{scheduleId}")
	public HttpEntity<ScheduleResponse> getInfo(@PathVariable Long scheduleId) {

		Schedule entity = scheduleService.findById(scheduleId);
		return ResponseEntity.ok(ScheduleResponse.fromEntity(entity));
	}

	@GetMapping("/userinfo/{userId}")
	public HttpEntity<Page<ScheduleResponse>> getList(@PathVariable Long userId,
													  @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<ScheduleResponse> schedules = scheduleService.findAllByUserId(userId, pageable);
		return ResponseEntity.ok(schedules);
	}

	@GetMapping("/all")
	public HttpEntity<Page<ScheduleResponse>> getAllSchedulesList(Pageable pageable) {

		Page<ScheduleResponse> schedules = scheduleService.findAll(pageable, Status.PERMIT);
		return ResponseEntity.ok(schedules);
	}

	@PostMapping("/save")  //저장
	public HttpEntity<String> save(@RequestBody @Valid ScheduleRequest request,
								   HttpServletRequest ServletRequest) {

		String email = getEmailByToken(ServletRequest);
		scheduleService.save(request, email);
		return ResponseEntity.ok("저장되었습니다.");
	}

	@PostMapping("/{scheduleId}/edit")  //수정
	public ResponseEntity<ScheduleResponse> edit(@PathVariable Long scheduleId,
												 @RequestBody @Valid ScheduleRequest request,
												 HttpServletRequest servletRequest) {

		String email = getEmailByToken(servletRequest);

		// TODO : email이 작성자와 동일한지

		ScheduleResponse response = scheduleService.edit(scheduleId, request, email);

		return ResponseEntity.ok().body(response);  // 수정하고 ScheduleResponse 반환
	}

	@PostMapping("/{scheduleId}/delete") //삭제
	public ResponseEntity<Void> delete(@PathVariable Long scheduleId,
									   HttpServletRequest servletRequest) {
		String email = getEmailByToken(servletRequest);

		// TODO : email이 작성자와 동일한지

		scheduleService.delete(email, scheduleId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userId}/day")
	public ResponseEntity<List<ScheduleResponse>> getSchedulesByDay(
		@PathVariable Long userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		List<ScheduleResponse> schedules = scheduleService.getSchedulesByDay(date == null ? LocalDate.now() : date,
																			 userId);

		if (schedules.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(schedules);
	}

	@GetMapping("/{userId}/week")
	public ResponseEntity<List<ScheduleResponse>> getSchedulesByWeek(
		@PathVariable Long userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		List<ScheduleResponse> schedules = scheduleService.getSchedulesByWeek(date == null ? LocalDate.now() : date,
																			  userId);

		if (schedules.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(schedules);
	}

	@GetMapping("/{userId}/month")
	public ResponseEntity<List<ScheduleResponse>> getSchedulesByMonth(
		@PathVariable Long userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		List<ScheduleResponse> schedules = scheduleService.getSchedulesByMonth(date == null ? LocalDate.now() : date,
																			   userId);

		if (schedules.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(schedules);
	}

	@GetMapping("/{userId}/year")
	public ResponseEntity<List<ScheduleResponse>> getSchedulesByYear(
		@PathVariable Long userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		List<ScheduleResponse> schedules = scheduleService.getSchedulesByYear(date == null ? LocalDate.now() : date,
																			  userId);

		if (schedules.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(schedules);
	}

	@GetMapping("/excel") // http://8080/schedules/export?role=ROLE_USER
	public ResponseEntity<Resource> exportSchedulesToExcel(@RequestParam Role role, HttpServletRequest request) {

		String token = request.getHeader("Authorization").split(" ")[1].trim();

		String email = JwtUtils.getEmail(token, secretKey);

		User user = userService.getUserByEmail(email);
		Long userId = user.getId();

		List<Schedule> schedules = scheduleService.getSchedulesByRole(role, userId);

		ByteArrayInputStream excelFile = scheduleService.createExcelFile(schedules);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=schedules.xlsx");

		return ResponseEntity.ok()
							 .headers(headers)
							 .body(new InputStreamResource(excelFile));
	}

	private String getEmailByToken(HttpServletRequest ServletRequest) {
		String token = ServletRequest.getHeader("Authorization").split(" ")[1].trim();
		String email = JwtUtils.getEmail(token, secretKey);
		return email;
	}
}



