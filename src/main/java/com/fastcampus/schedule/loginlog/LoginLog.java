package com.fastcampus.schedule.loginlog;

import javax.persistence.*;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.user.domain.User;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "login_log")
public class LoginLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private User user;
	private String agent;
	private String clientIp;

	@Column(name = "login_time")
	private LocalDateTime loginTime;

	protected LoginLog() {
	}
	private LoginLog(User user, String agent, String clientIp, LocalDateTime loginTime) {
		this.user = user;
		this.agent = agent;
		this.clientIp = clientIp;
		this.loginTime = loginTime;
	}
	public static LoginLog of(User user, String agent, String clientIp, LocalDateTime loginTime) {
		return new LoginLog(user, agent, clientIp, loginTime);
	}

}
