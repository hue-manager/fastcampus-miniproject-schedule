package com.fastcampus.schedule.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import com.fastcampus.schedule.domain.constant.Role;

import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String memberName;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String phoneNumber;
	@Enumerated(EnumType.STRING)
	private Role role;
	@OneToMany(mappedBy = "member")
	private List<Schedule> schedules = new ArrayList<>();
	@OneToMany(mappedBy = "member")
	private List<LoginLog> logs = new ArrayList<>();

	protected Member() {
	}

	private Member(String email, String memberName, String nickname, String password, String phoneNumber, Role role) {
		this.email = email;
		this.memberName = memberName;
		this.nickname = nickname;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public static Member of(String email,
							String memberName,
							String nickname,
							String password,
							String phoneNumber,
							Role role) {
		return new Member(email, memberName, nickname, password, phoneNumber, role);
	}
}
