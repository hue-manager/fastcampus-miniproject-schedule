package com.fastcampus.schedule.util;

import java.time.LocalDate;

public class Utils {

	public static LocalDate stringToDate(String rowDate) {
		int year = Integer.parseInt(rowDate.substring(0, 4));
		int month = Integer.parseInt(rowDate.substring(4, 6));
		int day = Integer.parseInt(rowDate.substring(6));
		return LocalDate.of(year, month, day);
	}
}
