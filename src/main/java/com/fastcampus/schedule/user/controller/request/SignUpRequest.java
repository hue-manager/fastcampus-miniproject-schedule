package com.fastcampus.schedule.user.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fastcampus.schedule.user.domain.User;
import com.fastcampus.schedule.user.domain.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 2, max = 6)
    //@Pattern(regexp = "^[가-힣]{2,6}$")
    private String userName;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
            , message = "비밀번호는 필수항목입니다, 비밀번호는 영어와 숫자로 포함해서 8~15자리 이내로 입력해주세요.")
    private String password;

    @NotNull
    private String password2;
    @NotNull
    @Pattern(regexp = "^01(?:0|1|6)-(?:\\d{3}|\\d{4})-\\d{4}$", message = "입력 예 : 010-0000-0000")
    private String phoneNumber;

    @NotNull
    private String position;

    @NotNull
    private String department;

    public static User toEntity(SignUpRequest request, PasswordEncoder encoder) {
        return User.of(request.email,
                request.userName,
                encoder.encode(request.password),
                request.phoneNumber,
                Role.DEFAULT,
                request.position,
                request.department);
    }
}
