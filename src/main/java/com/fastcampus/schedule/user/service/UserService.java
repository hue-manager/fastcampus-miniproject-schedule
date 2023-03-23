package com.fastcampus.schedule.user.service;

import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.UserRequest;
import com.fastcampus.schedule.user.domain.UserResponse;
import com.fastcampus.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 권한 수정
    public UserResponse editUserRole(Long userId, UserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("해당하는 유저가 없습니다"));
        if(!user.getRole().equals(request.getRole())){
            user.setRole(request.getRole());
        }
        return UserResponse.fromEntity(user);
    }

    // 유저 정보 수정
    public UserResponse editUserInfo(Long userId, UserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("해당하는 유저가 없습니다"));
        if(!user.getUserName().equals(request.getUserName())){
            user.setUserName(request.getUserName());
        }
        if(!user.getEmail().equals(request.getEmail())){
            user.setEmail(request.getEmail());
        }
        return UserResponse.fromEntity(user);
    }

    //TODO 전체 조회
    /*
    public Page<UserResponse> getUserList(Role role, Pageable pageable) {
        // null인 경우는 전체
        if (role == null)
            return userRepository.findAllUsersByRole(null, pageable);
        .map(UserResponse::fromEntity);

        // 아니면 param
    return switch(role){
            case ROLE_USER -> userRepository.findAllUsersByRole(role, pageable);
                   // .map(UserResponse::fromEntity)
            case ROLE_ADMIN -> userRepository.findAllUsersByRole(role, pageable);
                   // .map(UserResponse::fromEntity)
        };
    }

     */

    // 한명 조회
    public UserResponse getUser(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다"));
    }



}