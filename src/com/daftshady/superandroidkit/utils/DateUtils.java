package com.daftshady.superandroidkit.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.net.ParseException;

/**
 * Provides `Date` utility methods.
 * @author parkilsu
 *
 */
public class DateUtils {
	public static final String DATE_FORMAT = "yyyy-MM-dd G HH:mm:ss z";
	
	/**
	 * Returns current time with default `DATE_FORMAT`
	 * @param dateFormat
	 * @return
	 */
	public static String getCurrentTime(String dateFormat) {
		return formatDate(Calendar.getInstance().getTime(), dateFormat);
	}
	
	/**
	 * Returns time delta from current time with `dateFormat`
	 * @param delta
	 * 		millis of time delta
	 * @param dateFormat
	 * 		Date format
	 * @return
	 *		Formatted time delta string 
	 */
	public static String getTimeDelta(long delta, String dateFormat) {
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(current.getTimeInMillis() + delta);
		return formatDate(current.getTime(), dateFormat);
	}
	
	/**
	 * Returns date delta from current time with `dateFormat`
	 * @param delta
	 * 		Value for `days` delta.
	 * @param dateFormat
	 * @return
	 */
	public static String getDateDelta(int delta, String dateFormat) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, delta);
		return formatDate(current.getTime(), dateFormat);
	}
	
	/**
	 * Format date with default `DATE_FORMAT` and default locale `Locale.KOREA`
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DATE_FORMAT);
	}
	
	/**
	 * Format date with `dateFormat` given and default locale `Locale.KOREA`
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String formatDate(Date date, String dateFormat) {
		return formatDate(date, dateFormat, Locale.ENGLISH);
	}
	
	/**
	 * Format date
	 * @param date
	 * 		`Date` formatted to
	 * @param dateFormat
	 * 		Date format like 'yyyy-MM-dd'
	 * @param locale
	 * 		Locale for date format.
	 * @return
	 *		Formatted date string 
	 */
	public static String formatDate(Date date, String dateFormat, Locale locale) {
		SimpleDateFormat format = 
				new SimpleDateFormat(dateFormat, locale);
		return format.format(date);	
	}
	
	public static Date fromString(String dateStr) throws java.text.ParseException{
		return fromString(dateStr, DATE_FORMAT);
	}

	public static Date fromString(String dateStr, String dateFormat) 
			throws java.text.ParseException{
		return fromString(dateStr, dateFormat, Locale.ENGLISH);
	}
	
	public static Date fromString(String dateStr, String dateFormat, Locale locale)
			throws java.text.ParseException{
		SimpleDateFormat format = 
				new SimpleDateFormat(dateFormat, locale);
		return format.parse(dateStr);
	}
}