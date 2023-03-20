package com.fastcampus.schedule.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Getter
@Entity
public class LoginLog extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	private LocalDateTime loginTime;
	private String agent;
	private String clientIp;

	protected LoginLog() {
	}

	private LoginLog(User user, LocalDateTime loginTime, String agent, String clientIp) {
		this.user = user;
		this.loginTime = loginTime;
		this.agent = agent;
		this.clientIp = clientIp;
	}

	public static LoginLog of(User user,
							  LocalDateTime loginTime,
							  String agent,
							  String clientIp) {
		return new LoginLog(user, loginTime, agent, clientIp);
	}
}
