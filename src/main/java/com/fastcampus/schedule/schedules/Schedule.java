package com.fastcampus.schedule.schedules;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fastcampus.schedule.BaseEntity;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.schedules.util.Period;
import com.fastcampus.schedule.user.domain.User;

import lombok.Getter;

@Getter
@Entity
public class Schedule extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	@Enumerated(EnumType.STRING)
	private Category category;
	@Column(nullable = false)
	private LocalDate startDate;
	@Column(nullable = false)
	private LocalDate endDate;
	private String memo;

	protected Schedule() {
	}

	private Schedule(User user, Category category, LocalDate startDate, LocalDate endDate, String memo) {
		this.user = user;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
		this.memo = memo;
	}

	public static Schedule of(User user,
							  Category category,
							  LocalDate startDate,
							  LocalDate endDate,
							  String memo) {
		return new Schedule(user, category, startDate, endDate, memo);
	}

	public boolean isOverlapped(Period period) {
		return Period.of(this.getStartDate(), this.getEndDate()).isOverlapped(period);
	}
}
