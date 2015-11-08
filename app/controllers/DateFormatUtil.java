package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

	/**
	 * 将字符串时间转换为long
	 * @param stime
	 * @return
	 */
	public static long StringDate2Long(String stime) {
		Date date = null;
		if (stime != null && stime.trim().length() > 10) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		} else if (stime != null && stime.trim().length() <= 10) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat();
			try {
				date = sdf.parse(stime);
			} catch (ParseException e) {
				System.out.println("Time format error.");
			}
		}
		return DateFormatUtil.date2Long(date);
	}

	/**
	 * 转为时间戳
	 * @param date
	 * @return
	 */
	public static long date2Long(Date date) {
		long longTime = -1;
		if (date != null)
			longTime = date.getTime() / 1000;
		return longTime;
	}

	/**
	 * 时间戳转为yyyy-MM-dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static String long2StringDate(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(DateFormatUtil.long2Date(time));
	}

	/**
	 * 时间戳转为MM-dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static String long2StringDateNoYear(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm:ss");
		return sf.format(DateFormatUtil.long2Date(time));
	}

	/**
	 * 时间戳转为yyyy-MM-dd
	 * @param time
	 * @return
	 */
	public static String long2StringDateNoTime(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(DateFormatUtil.long2Date(time));
	}

	/**
	 * 将时间戳转换为MM-dd
	 * @param time
	 * @return
	 */
	public static String long2StringDateNoYearAndTime(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
		return sf.format(DateFormatUtil.long2Date(time));
	}

	/**
	 * 将时间戳转换为Date
	 * @param time
	 * @return
	 */
	public static Date long2Date(long time) {
		return new Date(time * 1000);
	}

	/**
	 * Date转为yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 获取当前时间yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String currentTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 得到指定时间几天前的时间
	 * @param d
	 * @param day
	 * @return Date
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天前的时间
	 * @param d
	 * @param day
	 * @return String
	 */
	public static String getDayBefore(int day) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayString = sf.format(getDateBefore(new Date(), day));
		todayString = todayString.substring(0, 10) + " 00:00:00";
		return todayString;
	}

	/**
	 * 得到指定时间几天后的时间
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 获取前几天的日期yyyy-mm-dd
	 * @param day
	 * @return
	 */
	public static String getBeforeDay(int day) {
		return DateFormatUtil.getDayBefore(1).substring(0, 10);
	}

	public static void main(String[] args) {
		System.out.println(date2String(long2Date(1372386437l)));
	}
}