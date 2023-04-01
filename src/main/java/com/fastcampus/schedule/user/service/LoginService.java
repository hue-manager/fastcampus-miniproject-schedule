package com.fastcampus.schedule.user.service;

import static com.fastcampus.schedule.exception.constant.ErrorCode.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.schedule.config.jwt.JwtUtils;
import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.exception.constant.ErrorCode;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	// private final UserRedisRepository userRedisRepository;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.expired-time-ms}")
	private Long expiredTimeMs;

	public String login(String email, String password) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ScheduleException(USER_NOT_FOUND,
																							  "존재하지 않는 이메일 입니다."));
		if (!encoder.matches(password, user.getPassword())) {
			throw new ScheduleException(ErrorCode.INVALID_PASSWORD, "비밀번호를 확인해주세요.");
		}

		// userRedisRepository.setRedisUser(user);
		return JwtUtils.createToken(user);
	}

	public void logout() {
		// register token in redis to blacklist until expired time

	}
}
