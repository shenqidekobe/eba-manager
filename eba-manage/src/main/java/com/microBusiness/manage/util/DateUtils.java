package com.microBusiness.manage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.PrettyTime;

public class DateUtils{

	private static DateUtils instance = null;
	
	private final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public final static String DEFAULT_TIMENO_FORMAT = "yyyyMMddHHmmss";
	public final static String DATE_FORMAT = "yyyyMMdd";
	public final static String DATE_FORMAT_LINE = "yyyy-MM-dd";
	public final static String DATE_FORMAT_CHINA_FONT = "yyyy年MM月dd日";

	private DateUtils(){}
	
	public synchronized static DateUtils newInstance(){
		if(instance == null){
			instance = new DateUtils();
		}
		return instance;
	}
	
	/**
	 * 日期格式化
	 * @param format 默认为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String convertToString(String format){
		DateTime d = new DateTime();
		if(StringUtils.isBlank(format)){
			return d.toString(DEFAULT_TIME_FORMAT);
		}
		return d.toString(format);
	}
	
	/**
	 * 转换日期格式
	 * @param src exp: 2013-03-31 11:20:50
	 * @param sdf exp: yyyy-MM-dd HH:mm:ss
	 * @param desc exp : yyyy-MM-dd
	 * @return
	 */
	public String convertToString(String src,String sdf,String desc){
		SimpleDateFormat format = new SimpleDateFormat(sdf);
		DateTime d = null;
		try {
			d = new DateTime(format.parse(src));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d.toString(desc);
	}
	
	
	/**
	 * 日期格式化
	 * @param date 默认为 new date
	 * @param format 默认为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String convertToString(Date date){
		return convertToString(date,null);
	}
	/**
	 * 日期格式化
	 * @param date 默认为 new date
	 * @param format 默认为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String convertToString(Date date, String format){
		DateTime d = (date == null) ? DateTime.now() : new DateTime(date);
		if(StringUtils.isBlank(format)){
			return d.toString(DEFAULT_TIME_FORMAT);
		}
		return d.toString(format);
	}
	
	/**
	 * 返回一个日期
	 * @param date String类型日期
	 * @param format 格式化模版
	 * @return
	 */
	public Date convertToDate(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d1 = null;
		try {
			d1 = sdf.parse(date);
		} catch (ParseException e) {
		}
		return d1;
	}
	/**
	 * 返回一个日期
	 * @param time long
	 * @param format 格式化模版
	 * @return
	 */
	public String convertToDate(Long time) {
		return convertToDate(time,DEFAULT_TIME_FORMAT);
	}
	/**
	 * 返回一个日期
	 * @param time long
	 * @param format 格式化模版
	 * @return
	 */
	public String convertToDate(Long time, String format) {
		DateTime d = new DateTime(time);
		return d.toString(format);
	}
	
	/**
	 * 返回一个long的时间
	 * @param date  2013-01-01 08:06:09
	 * @param format 默认为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public Long convertToLong(String date, String format) {
		Date d = convertToDate(date,format);
		return d.getTime();
	}
	
	public Long convertToLong(String date) {
		return convertToLong(date,DEFAULT_TIME_FORMAT);
	}
	
	/**
	 * 计算2个时间相差多久，类似于论坛显示的发帖时间
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String timeTifference(Date date){
        PrettyTime p = new PrettyTime(Locale.SIMPLIFIED_CHINESE);
		return p.format(date).replaceAll(" ", "");
	}
	
	/**
	 * 过期时间 - 当前时间 相差多少年
	 * @param startDate 过期时间
	 * @return int 过期时间 - 当前时间
	 */
	public int yearsBetween(Date startDate){
		DateTime sd = new DateTime(startDate.getTime());
		DateTime ed = new DateTime();
		int rs = Years.yearsBetween(ed, sd).getYears();
		return rs;
	}
	
	/**
	 * 计算2个时间相差多少年
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return int 结束时间 - 开始时间
	 */
	public int yearsBetween(Date startDate, Date endDate){
		DateTime sd = new DateTime(startDate.getTime());
		DateTime ed = new DateTime(endDate.getTime());
		return Years.yearsBetween(sd, ed).getYears();
	}
	/**
	 * 计算2个时间相差多少天
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return int 结束时间 - 开始时间
	 */
	public static int daysBetween(Date startDate, Date endDate){
		DateTime sd = new DateTime(startDate.getTime());
		DateTime ed = new DateTime(endDate.getTime());
		return Days.daysBetween(sd, ed).getDays();
	}
	
	/**
	 * 修改时间的天数，基于当前时间的天数增减
	 * @param date 当前时间
	 * @param days 增减天数   正负区分
	 * @return
	 */
	public static Date plusDays(Date date,int days){
		DateTime time=new DateTime(date.getTime());
		return time.plusDays(days).toDate();
	}
	/**
     * 生成当前时间距离格林威治时间的毫秒数
     * 
     * @return
     * @author jianming.jiangjm
     * @date Jul 21, 2012
     */
    public static long getSencondFrom1970() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    /**
     * 将指日期转为固定格式的字符串日期，日期类型为String
     * 
     * @param strDate
     * @param yyyyMMdd 需要转为的日期格式
     * @return
     * @throws Exception
     */
    public static String formatDateToString(java.util.Date date, DateformatEnum dateFormat) {
        String strDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        strDate = sdf.format(date);
        return strDate;
    }

    /**
     * @param strDate
     * @param dateFormat
     * @return
     * @throws Exception
     * @author jianming.jiangjm
     * @date Jul 21, 2012
     */
    public static java.util.Date formatStringToDate(String strDate, DateformatEnum dateFormat) {
        java.util.Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat.getFormat());
        try {
            d = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * @param dateFormat
     * @return
     * @author jianming.jiangjm
     * @date Jul 21, 2012
     */
    public static String getCurrentDateStr(DateformatEnum dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat.getFormat());
        String mDateTime = formatter.format(cal.getTime());
        return mDateTime;
    }

    /**
     * @param dateFormat
     * @return
     * @throws Exception
     * @author jianming.jiangjm
     * @date Jul 21, 2012
     */
    public static Date getCurrentDate(DateformatEnum dateFormat) {
        Date date =  formatStringToDate(getCurrentDateStr(dateFormat), dateFormat);
        return date;
    }
	private static String[] WEEKS = {"7","1","2","3","4","5","6"};
    public static String getDayOfWeek(Date date){
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return WEEKS[w] ;*/
		DateTime dateTime = new DateTime(date);
		return String.valueOf(dateTime.dayOfWeek().get());
	}
	private static String[] DAY_IN_WEEK = {"周一","周二","周三","周四","周五","周六","周天"};
	public static String toWeekDay(String day){
		if(StringUtils.isEmpty(day)){
			return "" ;
		}
		int d = Integer.valueOf(day) ;

		return DAY_IN_WEEK[d-1] ;
	}

	public static int compareTo(Date date , Date date1){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR , 0);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND , 0);
        calendar.set(Calendar.MILLISECOND , 0);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(Calendar.HOUR , 0);
        calendar1.set(Calendar.MINUTE , 0);
        calendar1.set(Calendar.SECOND , 0);
        calendar1.set(Calendar.MILLISECOND , 0);

        return calendar.compareTo(calendar1) ;

    }

	/**
	 * 获取忽略秒的时间
	 * @param date
	 * @return
	 */
    public static Date getDateNoSec(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND , 0);
		calendar.set(Calendar.MILLISECOND , 0);
		return  calendar.getTime() ;
	}
	public static void main(String[] args) throws Exception {
//		String dateStart = "2013-03-31 11:20:50";
//        String dateStop = "2013-03-31 11:31:59";
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date d1 = format.parse(dateStart);
//        Date d2 = format.parse(dateStop);
//        
//        DateTime d = new DateTime();
//        System.err.println(d.toString("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(d.plusDays(-1).toString("yyyy-MM-dd HH:mm:ss"));
//        
//        System.err.println(DateUtils.newInstance().convertToString("2013-03-31 11:20:50","yyyy-MM-dd HH:mm:ss", "yyyyMMdd"));
        
        //System.err.println(DateUtils.newInstance().convertToLong("2013-09-01 00:00:00","yyyy-MM-dd HH:mm:ss"));
		
		Date nowTime = DateUtils.getCurrentDate(DateformatEnum.yyyyMMddHHmmss2) ;
	    //String createTime = DateUtils.formatDateToString(create,DateformatEnum.yyyyMMddHHmmss2);
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin=dfs.parse("2016-01-13 14:03:24");
		java.util.Date now = DateUtils.getCurrentDate(DateformatEnum.yyyyMMddHHmmss2);
		long between=(now.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
        
		long year = between/(24*3600*365);
		long day1=between/(24*3600);
		long hour1=between%(24*3600)/3600;
		long minute1=between%3600/60;
		long second1=between%60/60;
		
		
		PrettyTime p = new PrettyTime(Locale.CHINESE);
        System.out.println(p.format(begin));
		System.out.println(year+"年"+day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒");
		System.out.println(DateUtils.newInstance().convertToDate(1383840000000l));
	}
	
	public static int diffMonth(String startDate, String endDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.formatStringToDate(startDate, DateformatEnum.yyyyMM2));
		int startYear = cal.get(Calendar.YEAR);
		int startMonth = cal.get(Calendar.MONTH);
		cal.setTime(DateUtils.formatStringToDate(endDate, DateformatEnum.yyyyMM2));
		int endYear = cal.get(Calendar.YEAR);
		int endMonth = cal.get(Calendar.MONTH);
		int result = 0;
		if(startYear == endYear){
			result = endMonth - startMonth;//两个日期相差几个月，即月份差
		}else{
			if(startMonth > endMonth){
				result=Math.abs(12*(startYear-endYear)+startMonth-endMonth);
			}else{
				result=12*(endYear-startYear)+endMonth-startMonth;
			}
		}
		return result;
	}

	public static int getCountOfWeekDay(DateTime startTime , DateTime endTime , int dayOfWeek){
		Days days = Days.daysBetween(startTime , endTime);
		int count = 0;
		for(int i=0,dffD = days.getDays() ; i<=dffD ; i++){
			DateTime temp = startTime.plusDays(i);
			System.out.println(temp.toString());
			if (temp.dayOfWeek().get() == dayOfWeek){
				count ++ ;
			}
		}

		return count ;
	}

	public static int getCountOfWeekDay1(DateTime startTime , DateTime endTime , int dayOfWeek){
		Weeks weeks = Weeks.weeksBetween(startTime , endTime);
		int diffWeek = weeks.getWeeks() == 0 ? 1 : weeks.getWeeks();

		Days days = Days.daysBetween(startTime , endTime);
		int mod = (days.getDays()+1)%7 ;
		int count = diffWeek;
		if(dayOfWeek == mod){
			count++;
		}
		return count ;
	}

	public static boolean isHoliday(Date date, String daily) {
		String dayOfWeek = getDayOfWeek(date);
		String[] dailys = StringUtils.split(daily, ",");
		for (String str : dailys) {
			if (str.equals(dayOfWeek)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isHoliday(DateTime date, String daily) {
		String dayOfWeek = String.valueOf(date.dayOfWeek().get());
		String[] dailys = StringUtils.split(daily, ",");
		for (String str : dailys) {
			if (str.equals(dayOfWeek)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 考情专用
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static DateTime toDate(String date , String pattern){
		if(StringUtils.isEmpty(date)){
			return new DateTime();
		}
		try {
			DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
			DateTime dateTime = DateTime.parse(date , format);
			return dateTime ;
		}catch (Exception e){
			e.printStackTrace();
			return new DateTime();
		}

	}
	
	/**
	 * 获取当天零点
	 */
	public static Date currentStartTime() {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取当天24点
	 * @return
	 */
	public static Date currentEndTime() {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期零点
	 */
	public static Date specifyDateZero(Date date) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期23:59:59
	 * @return
	 */
	public static Date specifyDatetWentyour(Date date) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取本周开始时间
	 * @return
	 */
	public static Date startThisWeek() {
		DateTime dateTime = new DateTime();
		return dateTime.withDayOfWeek(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取本周结束时间
	 * @return
	 */
	public static Date endOfTheWeek() {
		DateTime dateTime = new DateTime();
		return dateTime.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取上周开始时间
	 * @return
	 */
	public static Date lastWeekStartTime() {
		DateTime dateTime = new DateTime() ;
		DateTime last = dateTime.plusWeeks(-1);
		return last.withDayOfWeek(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取上周结束时间
	 * @return
	 */
	public static Date lastWeekEndTime() {
		DateTime dateTime = new DateTime() ;
		DateTime last = dateTime.plusWeeks(-1);
		return last.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取本月开始时间
	 * @return
	 */
	public static Date startThisMonth() {
		DateTime dateTime = new DateTime();
		return dateTime.dayOfMonth().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取本月结束时间
	 * @return
	 */
	public static Date theEndOfTheMonth() {
		DateTime dateTime = new DateTime();
		return dateTime.dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取上月开始时间
	 * @return
	 */
	public static Date lastMonthStartTime() {
		DateTime dateTime = new DateTime();
		return dateTime.plusMonths(-1).dayOfMonth().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取上月结束时间
	 * @return
	 */
	public static Date lastMonthEndTime() {
		DateTime dateTime = new DateTime();
		return dateTime.plusMonths(-1).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取某年某月开始时间
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static Date specifyMonthStartTime(int year , int month) {
		DateTime dateTime = new DateTime();
		return dateTime.withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 获取某年某月结束时间
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static Date specifyMonthEndTime(int year , int month) {
		DateTime dateTime = new DateTime();
		return dateTime.withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * 遍历两个时间段内所有的月份
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public static List<Date> getDateList(Date startDate , Date endDate) {
    	Calendar dd = Calendar.getInstance();//定义日期实例
    	dd.setTime(startDate);//设置日期起始时间
    	
    	List<Date> dates = new ArrayList<Date>();
    	while(dd.getTime().before(endDate)){//判断是否到结束日期
	    	dates.add(dd.getTime());
	    	dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
    	}
		return dates;
	}
	
	/**
	 * 获取指定日期的年
	 * @param date
	 * @return
	 */
	public static int getSpecifiedDateByYear(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.getYear();
	}
	
	/**
	 * 获取指定日期的月
	 * @param date
	 * @return
	 */
	public static int getSpecifiedDateByMonth(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.getMonthOfYear();
	}
}
