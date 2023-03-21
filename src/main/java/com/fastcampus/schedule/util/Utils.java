package com.fastcampus.schedule.util;

import java.time.LocalDate;

public class Utils {

	public static LocalDate stringToDate(String rowDate) {
		// TODO
		int year = 0;
		int month = 0;
		int day = 0;
		return LocalDate.of(year, month, day);
	}
}
