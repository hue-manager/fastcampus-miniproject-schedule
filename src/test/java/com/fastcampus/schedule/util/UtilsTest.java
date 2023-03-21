package com.fastcampus.schedule.util;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

	@Test
	void 문자열을_날짜로_변경() {
		String rowDate = "20230321";
		Assertions.assertThat(Utils.stringToDate(rowDate)).isEqualTo(LocalDate.of(2023,03,21));
	}
}
