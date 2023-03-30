package com.fastcampus.schedule.user.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoRequest {

	@Email(message = "유효하지 않은 이메일 형식입니다",
		regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
	private String email;

	@Size(min = 2, max = 10, message = "최소 2자에서 최대 10자로 입력해주세요")
	private String userName;

	public UserInfoRequest(String request) {
		if(request.contains("@")){
			this.email = request;
			this.userName = getUserName();
		}else{
			this.email = getEmail();
			this.userName = request;
		}
	}
}
