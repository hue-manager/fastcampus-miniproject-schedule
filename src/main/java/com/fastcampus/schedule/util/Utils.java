package com.fastcampus.schedule.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

	public static LocalDate stringToDate(String rowDate) {

		LocalDate localDate = LocalDate.parse(rowDate, DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd 형식
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		return LocalDate.of(year, month, day);
	}
}
