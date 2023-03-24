package com.fastcampus.schedule.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.UserResponse;

public interface UserRepository extends JpaRepository<User, Long> {

	Page<User> findAllUsersByRole(Role role, Pageable pageable);

	Optional<User> findByUserName(String userName);

    //회원 가입하려는 유저이름이 존재하는지 검사
    User findByUserName(String userName);

	Optional<User> findByEmail(String email);


}
