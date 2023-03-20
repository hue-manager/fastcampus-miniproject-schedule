package com.fastcampus.schedule.loginlog;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.user.User;

import lombok.Getter;

@Getter
@Entity
public class LoginLog extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	private String agent;
	private String clientIp;

	protected LoginLog() {
	}

	private LoginLog(User user, String agent, String clientIp) {
		this.user = user;
		this.agent = agent;
		this.clientIp = clientIp;
	}

	public static LoginLog of(User user, String agent, String clientIp) {
		return new LoginLog(user, agent, clientIp);
	}
}
