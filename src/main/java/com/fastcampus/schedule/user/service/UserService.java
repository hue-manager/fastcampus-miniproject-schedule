package com.fastcampus.schedule.user.service;

import static com.fastcampus.schedule.exception.constant.ErrorCode.*;

import com.fastcampus.schedule.user.repository.UserRedisRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.controller.requset.SignUpRequest;
import com.fastcampus.schedule.user.controller.requset.UserInfoRequest;
import com.fastcampus.schedule.user.controller.requset.UserRoleRequest;
import com.fastcampus.schedule.user.controller.response.UserResponse;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final UserRedisRepository userRedisRepository;

	//회원 가입
	public void signUp(SignUpRequest request) {
		userRepository.findByEmail(request.getEmail()).ifPresent(user -> new ScheduleException(DUPLICATED_EMAIL, ""));
		userRepository.save(SignUpRequest.toEntity(request, encoder));
	}

	// 권한 수정
	public UserResponse editUserRole(Long userId, UserRoleRequest request) {
		User user = checkExist(userId);
		if (!user.getRole().equals(request.getRole())) {
			user.setRole(request.getRole());
		}
		return UserResponse.fromEntity(user);
	}

	// 유저 정보 수정
	public UserResponse editUserInfo(Long userId, UserInfoRequest request) {
		User user = checkExist(userId);
		if (user.isNotSame(user.getUsername(), request.getUserName())) {
			user.setUserName(request.getUserName());
		}
		if (!user.getEmail().equals(request.getEmail())) {
			user.setEmail(request.getEmail());
		}
		return UserResponse.fromEntity(user);
	}

	// 전체 조회
	public Page<UserResponse> getUserList(Role role, Pageable pageable) {
		if (role != null) {
			userRepository.findAllUsersByRole(role, pageable)
						  .map(UserResponse::fromEntity);
		}
		return userRepository.findAll(pageable).map(UserResponse::fromEntity);
	}

	// 한명 조회
	public UserResponse getUser(Long userId) {
		return userRepository.findById(userId)
							 .map(UserResponse::fromEntity)
							 .orElseThrow(()
											  -> new ScheduleException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
	}

	public void confirm(Long userId) {
		User user = checkExist(userId);
		if (user.getRole().equals(Role.DEFAULT)) {
			user.setRole(Role.ROLE_USER);
		} else {
			throw new ScheduleException(USER_NOT_FOUND, "");
		}
	}

	// 유저 존재 여부 체크 공통 메서드
	public User checkExist(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()
																	-> new ScheduleException(USER_NOT_FOUND,
																							 USER_NOT_FOUND.getMessage()));
		return user;
	}

	public User getUserByEmail(String email) {
		return userRedisRepository.getRedisUser(email).orElseGet(()->
		 userRepository.findByEmail(email).orElseThrow(() -> new ScheduleException(USER_NOT_FOUND,
																						 USER_NOT_FOUND.getMessage())));
	}
}
