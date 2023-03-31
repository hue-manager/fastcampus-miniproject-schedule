package com.fastcampus.schedule.loginlog.service;
import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.loginlog.repository.LoginLogRepository;
import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.constant.Role;
import com.fastcampus.schedule.user.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
@ExtendWith(SpringExtension.class)
class LoginLogServiceTest {

    @InjectMocks
    private LoginLogService loginLogService;

    @Mock
    private LoginLogRepository loginLogRepository;


    @Test
    public void createLoginLogTest() {
        User user = User.of(1L, "email@example.com", "username", "password", "010-1234-5678", Role.ROLE_USER);
        String agent = "test agent";
        String clientIp = "192.168.0.1";
        LocalDateTime loginTime = LocalDateTime.now();

        loginLogService.createLoginLog(user, agent, clientIp, loginTime);


        verify(loginLogRepository).save(argThat(loginLog ->
                loginLog.getUser().equals(user) &&
                        loginLog.getAgent().equals(agent) &&
                        loginLog.getClientIp().equals(clientIp) &&
                        loginLog.getLoginTime().equals(loginTime)
        ));
    }


}
