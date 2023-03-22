package com.fastcampus.schedule.user.domain;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.user.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto extends BaseEntity {

    private Long id;
    private String email;
    private String userName;
    private String password;
    private String phoneNumber;
    private Role role;
    private List<Schedule> schedules;
    private List<LoginLog> logs;

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getSchedules(),
                user.getLogs()
        );
    }


}
