package com.fastcampus.schedule.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fastcampus.schedule.domain.constant.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Schedule extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id")
	private Member member;
	@Enumerated
	private Category category;
	private LocalDate startDate;
	private LocalDate endDate;
	private String memo;

	protected Schedule() {
	}

	private Schedule(Member member, Category category, LocalDate startDate, LocalDate endDate, String memo) {
		this.member = member;
		this.category = category;
		this.startDate = startDate;
		this.endDate = endDate;
		this.memo = memo;
	}

	public static Schedule of(Member member,
							  Category category,
							  LocalDate startDate,
							  LocalDate endDate,
							  String memo) {
		return new Schedule(member, category, startDate, endDate, memo);
	}
}
