package com.fastcampus.schedule.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Page<User> findAllUsersByRole(Role role, Pageable pageable);

	Optional<User> findByUserName(String userName);

	Optional<User> findByEmail(String email);

}
