package com.fastcampus.schedule.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fastcampus.schedule.domain.constant.Role;

import lombok.Getter;

@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String userName;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String phoneNumber;
	@Enumerated(EnumType.STRING)
	private Role role;
	@OneToMany(mappedBy = "user")
	private List<Schedule> schedules = new ArrayList<>();
	@OneToMany(mappedBy = "user")
	private List<LoginLog> logs = new ArrayList<>();

	protected User() {
	}

	private User(String email, String userName, String nickname, String password, String phoneNumber, Role role) {
		this.email = email;
		this.userName = userName;
		this.nickname = nickname;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public static User of(String email,
						  String userName,
						  String nickname,
						  String password,
						  String phoneNumber,
						  Role role) {
		return new User(email, userName, nickname, password, phoneNumber, role);
	}
}
