package com.lawencon.payroll.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDateTime toLocalDateTime(String time) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return LocalDateTime.parse(time, formatter);
	}
}
