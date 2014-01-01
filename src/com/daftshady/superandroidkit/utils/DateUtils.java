package com.daftshady.superandroidkit.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Provides `Date` utility methods.
 * @author parkilsu
 *
 */
public class DateUtils {
	public static final String DATE_FORMAT = "yyyy-MM-dd G HH:mm:ss z";
	
	public static String getCurrentTime() {
		return formatDate(Calendar.getInstance().getTime());
	}
	
	public static String getTimeDelta(long delta) {
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(current.getTimeInMillis() + delta);
		return formatDate(current.getTime());
	}
	
	private static String formatDate(Date date) {
		return formatDate(date, DATE_FORMAT);
	}
	
	private static String formatDate(Date date, String dateFormat) {
		SimpleDateFormat format = 
				new SimpleDateFormat(dateFormat, Locale.KOREA);
		return format.format(date);
	}
}
