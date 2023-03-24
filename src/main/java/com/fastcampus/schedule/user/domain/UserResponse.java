package com.fastcampus.schedule.user.domain;

import com.fastcampus.schedule.user.constant.Role;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserResponse {

	private String email;
	private String userName;
	private String phoneNumber;
	private Role role;

	public static UserResponse fromEntity(User user) {
		return UserResponse.builder()
						   .email(user.getEmail())
						   .userName(user.getUserName())
						   .phoneNumber(user.getPhoneNumber())
						   .role(user.getRole())
						   .build();
	}
}
