package com.fastcampus.schedule.schedules;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.schedules.constant.Status;
import com.fastcampus.schedule.schedules.util.Period;
import com.fastcampus.schedule.user.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Schedule extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	@Enumerated(EnumType.STRING)
	private Category category;
	@Column(nullable = false)
	private LocalDate startDate;
	@Column(nullable = false)
	private LocalDate endDate;
	@Enumerated(EnumType.STRING)
	@Setter
	private Status status;
	private String memo;

	protected Schedule() {
	}

	private Schedule(User user, Category category, LocalDate startDate, LocalDate endDate, Status status, String memo) {
		this.user = user;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.memo = memo;
	}

	public static Schedule of(User user,
							  Category category,
							  LocalDate startDate,
							  LocalDate endDate,
							  Status status,
							  String memo) {
		return new Schedule(user, category, startDate, endDate, status, memo);
	}

	public boolean isOverlapped(Period period) {
		return Period.of(this.getStartDate(), this.getEndDate()).isOverlapped(period);
	}

	public void deleteUser() {
		this.user = null;
	}
}
