package com.fastcampus.schedule.user.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.loginlog.LoginLog;
import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.user.domain.constant.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "USERS")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {

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
	@Setter
	private Integer vacationCount = 15;
	private String position;
	private String department;


	protected User() {
	}

	private User(String email, String userName, String password, String phoneNumber, Role role) {
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	private User(Long userId, String email, String userName, String password, String phoneNumber, Role role) {
		this.id = userId;
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

	public static User of(Long userId,
						  String email,
						  String userName,
						  String password,
						  String phoneNumber,
						  Role role
	) {
		return new User(userId, email, userName, password, phoneNumber, role);
	}

	public static User of(String email,
						  String userName,
						  String password,
						  String phoneNumber,
						  Role role,
						  String position,
						  String department
	) {
		User user = new User(email, userName, password, phoneNumber, role);
		user.position = position;
		user.department = department;
		return user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(Role.ROLE_USER.name()),
					   new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()));
	}

	@Override
	public String getUsername() {
		return userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean isSame(String name, String compare) {
		return name.equals(compare);
	}
}

