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
	@JoinColumn(name = "member_id")
	private Member member;
	private LocalDateTime loginTime;
	private String agent;
	private String clientIp;

	protected LoginLog() {
	}

	private LoginLog(Member member, LocalDateTime loginTime, String agent, String clientIp) {
		this.member = member;
		this.loginTime = loginTime;
		this.agent = agent;
		this.clientIp = clientIp;
	}

	public static LoginLog of(Member member,
							  LocalDateTime loginTime,
							  String agent,
							  String clientIp) {
		return new LoginLog(member, loginTime, agent, clientIp);
	}
}
