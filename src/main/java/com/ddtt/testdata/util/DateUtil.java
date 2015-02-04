package com.ddtt.testdata.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static String DATE_FORMAT_yyyyMMdd = "yyyy-MM-dd";

	public static String getCurrentDateYyyyMmDd() {
		SimpleDateFormat dfYyyyMmDd = new SimpleDateFormat(DATE_FORMAT_yyyyMMdd);
		String result = dfYyyyMmDd.format(new Date());
		dfYyyyMmDd = null;
		return result;
	}

	public static Date getSystemDate() {
		String inputStr = getCurrentDateYyyyMmDd();
		try {
			return new SimpleDateFormat(DATE_FORMAT_yyyyMMdd).parse(inputStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Timestamp getSystemTime() {
		return new Timestamp(new Date().getTime());
	}

	public static Date getDateByDays(Date date, double days) {
		long millisecond = (long) (days * 24 * 60 * 60 * 1000);
		date.setTime(date.getTime() + millisecond);
		return date;
	}
}
