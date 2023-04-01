package com.fastcampus.schedule.loginlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.schedule.loginlog.LoginLog;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
