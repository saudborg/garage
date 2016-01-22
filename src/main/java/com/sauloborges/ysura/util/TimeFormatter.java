package com.sauloborges.ysura.util;

import java.util.Calendar;

/**
 * Class used for format time in a patter 2016-01-22 13:40
 * @author sauloborges
 *
 */
public class TimeFormatter {

	public static String formatTime(Calendar time) {
		int year = time.get(Calendar.YEAR);
		int month = time.get(Calendar.MONTH) + 1; // Note: zero based!
		int day = time.get(Calendar.DAY_OF_MONTH);
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int minute = time.get(Calendar.MINUTE);

		String formatedTime = String.format("%d-%02d-%02d %02d:%02d", year, month, day, hour, minute);
		return formatedTime;
	}

}
