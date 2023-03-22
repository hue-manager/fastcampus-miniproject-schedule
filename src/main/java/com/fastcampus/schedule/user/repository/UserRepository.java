package com.fastcampus.schedule.user.repository;

import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 일반유저리스트만 or 관리자리스트만 or 전체리스트
    Page<UserResponse> findAllUsersByRole(Role role, Pageable pageable);


}
