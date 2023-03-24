package com.fastcampus.schedule.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.user.constant.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	@Column(nullable = false, unique = true)
	@Setter
	private String email;
	@Column(nullable = false)
	@Setter
	private String userName;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String phoneNumber;
	@Enumerated(EnumType.STRING)
	@Setter
	private Role role;
	@OneToMany(mappedBy = "user")
	private List<Schedule> schedules = new ArrayList<>();
	@OneToMany(mappedBy = "user")
	private List<LoginLog> logs = new ArrayList<>();

	protected User() {
	}

	private User(String email, String userName, String password, String phoneNumber, Role role) {
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public static User of(String email,
						  String userName,
						  String password,
						  String phoneNumber,
						  Role role) {
		return new User(email, userName, password, phoneNumber, role);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return Objects.equals(getId(), user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}

