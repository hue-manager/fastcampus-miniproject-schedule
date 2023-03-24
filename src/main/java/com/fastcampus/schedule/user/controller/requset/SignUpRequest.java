package com.fastcampus.schedule.user.controller.requset;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;

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
	@Length(min = 2, max = 10)
	private String userName;

	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
	private String password;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
	private String phoneNumber;

	public static User toEntity(SignUpRequest request, PasswordEncoder encoder) {
		return User.of(request.email,
					   request.userName,
					   encoder.encode(request.password),
					   request.phoneNumber,
					   Role.DEFAULT);
	}
}
