package com.fastcampus.schedule.loginlog.repository;

import com.fastcampus.schedule.loginlog.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
