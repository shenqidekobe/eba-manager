package com.microBusiness.manage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author xhs
 *
 */
public class DateUtil {

	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * 
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if (null == date) {
			return 0;
		}
		return date.getTime() / 1000;
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 
	 * @param timeSpace
	 *            时间间隔：毫秒
	 * @return
	 */
	public static String getAddTime(Long timeSpace) {
		long currentTime = System.currentTimeMillis() + timeSpace;
		Date now = new Date(currentTime);
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 
	 * @param time_start
	 *            开始时间
	 * @param timeSpace
	 *            时间间隔：毫秒
	 * @return
	 */
	public static String getAddTime(String time_start, Long timeSpace) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = sdf.parse(time_start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long currentTime = date.getTime() + timeSpace;
		Date now = new Date(currentTime);
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 时间转换成字符串
	 * 
	 * @param date
	 *            时间
	 * @param formatType
	 *            格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
