package com.fastcampus.schedule.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoRequest {

    @Email(message = "유효하지 않은 이메일 형식입니다",
            regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NotBlank
    private String email;

    @Size(min=2, max=10 , message="최소 2자에서 최대 10자로 입력해주세요")
    @NotBlank
    private String userName;
}
