package com.fastcampus.schedule.user.domain;

import com.fastcampus.schedule.user.constant.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRoleRequest {

    @NotEmpty
    private Role role;

    public UserRoleRequest(Role role) {
        this.role = role;
    }
}
