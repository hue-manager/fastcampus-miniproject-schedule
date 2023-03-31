package com.fastcampus.schedule.user.service;

import static com.fastcampus.schedule.exception.constant.ErrorCode.*;

import com.fastcampus.schedule.exception.constant.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.schedule.exception.ScheduleException;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.controller.requset.SignUpRequest;
import com.fastcampus.schedule.user.controller.requset.UserInfoRequest;
import com.fastcampus.schedule.user.controller.requset.UserRoleRequest;
import com.fastcampus.schedule.user.controller.response.UserResponse;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    // private final UserRedisRepository userRedisRepository;

    //회원 가입
    public void signUp(SignUpRequest request) {
        checkEmailDuplicated(request.getEmail());
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
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            user.setUserName(request.getUserName());
        } else if (request.getUserName() == null || request.getUserName().isEmpty()) {
            checkEmailDuplicated(request.getEmail());
            user.setEmail(request.getEmail());
        } else {
            user.setUserName(request.getUserName());
            user.setEmail(request.getEmail());
        }
        return UserResponse.fromEntity(user);
    }

    // 전체 조회
    public Page<UserResponse> getUserList(Role role, Pageable pageable) {
        if (role != null) {
            return userRepository.findAllUsersByRole(role, pageable)
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
        return userRepository.findByEmail(email).orElseThrow(()
                -> new ScheduleException(USER_NOT_FOUND,
                USER_NOT_FOUND.getMessage()));
    }

    // 이메일 중복 체크
    public Boolean checkEmailDuplicated(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new ScheduleException(DUPLICATED_EMAIL, DUPLICATED_EMAIL.getMessage());
                });
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(()
                -> new ScheduleException(USER_NOT_FOUND,
                USER_NOT_FOUND.getMessage()));
    }
}
