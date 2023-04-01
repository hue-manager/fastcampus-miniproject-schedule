package com.fastcampus.schedule.util;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fastcampus.schedule.schedules.constant.Category;

public class UtilsTest {

	@Test
	void 날짜차이() {
		LocalDate startDate = LocalDate.of(2023,03,23);
		LocalDate endDate = LocalDate.of(2023,03,25);
		System.out.println(startDate.compareTo(endDate));
	}

	@Test
	void 이넘() {
		System.out.println(Category.VACATION.name());
	}
}
