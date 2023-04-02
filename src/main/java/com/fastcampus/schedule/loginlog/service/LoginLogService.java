package com.fastcampus.schedule.loginlog.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.loginlog.repository.LoginLogRepository;
import com.fastcampus.schedule.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginLogService {
	private final LoginLogRepository loginLogRepository;

	public void createLoginLog(User user, String agent, String clientIp, LocalDateTime loginTime) {
		LoginLog loginLog = LoginLog.of(user, agent, clientIp, loginTime);
		loginLogRepository.save(loginLog);
	}

}
