package utils.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author Forrest
 */
public class TimeUtil {

	/**
	 * 每天的毫秒数
	 */
	public static final long DAY = TimeUnit.DAYS.toMillis(1);
	/**
	 * 每小时的毫秒数
	 */
	public static final long HOUR = TimeUnit.HOURS.toMillis(1);
	/**
	 * 每分的毫秒数
	 */
	public static final long MINUTE = TimeUnit.MINUTES.toMillis(1);
	/**
	 * 每秒的毫秒数
	 */
	public static final long SECOND = TimeUnit.SECONDS.toMillis(1);
	// 一小时的分钟数
	public static final long MINUTES_OF_HOUR = HOUR / MINUTE;

	public static final long SECONDS_OF_MINUTE = MINUTE / SECOND;
	/**
	 * 一周的天数
	 */
	public static final int DAY_OF_WEEK = 7;

	/**
	 * 一天的小时数
	 */
	public static final int HOUR_OF_DAY = 24;

	/**
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT1);

	private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");

	private static final TimeZone UTC8_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

	/**
	 * 指定逻辑时区为北京时区
	 */
	public static String logicTimeZone = "GMT";

	private static TimeZone getTimeZone() {
		if (logicTimeZone.equals("GMT+8")) {
			return UTC8_TIME_ZONE;
		} else {
			return UTC_TIME_ZONE;
		}
	}

	private static Calendar getCalendarByTimeStamp(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(getTimeZone());
		calendar.setTimeInMillis(time);
		return calendar;
	}

	/**
	 * 以0点为界， 只要跨越零点即算作一天， 比如 12-1 23:00 和12-2 01:00相差1天， 输入参数start < end 返回正数, start > end 返回负数
	 */
	private static int getSoFarWentDays(Calendar start, Calendar end) {
		int sign = start.before(end) ? 1 : -1;
		if (end.before(start)) {
			Calendar tmp = end;
			end = start;
			start = tmp;
		}
		int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
		if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
			Calendar cloneSt = (Calendar) start.clone();
			while (cloneSt.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
				days += cloneSt.getActualMaximum(Calendar.DAY_OF_YEAR);
				cloneSt.add(Calendar.YEAR, 1);
			}
		}
		return days * sign;
	}

	public static int getSoFarWentDays(Timestamp start, Timestamp end) {
		Calendar calendarStart = getCalendarByTimeStamp(start.getTime());
		Calendar calendarEnd = getCalendarByTimeStamp(end.getTime());
		return getSoFarWentDays(calendarStart, calendarEnd);
	}

	/**
	 * 得到相差分钟数 start > end 返回负数
	 */
	public static long getSoFarWentMinutes(Timestamp start, Timestamp end) {
		return (end.getTime() - start.getTime()) / MINUTE;
	}

	/**
	 * 得到相差秒数 start > end 返回负数
	 */
	public static long getSoFarWentSeconds(Timestamp start, Timestamp end) {
		return (end.getTime() - start.getTime()) / SECOND;
	}

	/**
	 * 是否是同一天
	 */
	public static boolean isSameDay(Timestamp start, Timestamp end) {
		Calendar st = getCalendarByTimeStamp(start.getTime());
		Calendar et = getCalendarByTimeStamp(end.getTime());
		return st.get(Calendar.YEAR) == et.get(Calendar.YEAR) && st.get(Calendar.MONTH) == et.get(Calendar.MONTH) && st.get(Calendar.DAY_OF_MONTH) == et.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断start和end是否在同一个星期内(周一为一周开始)
	 */
	public static boolean isSameWeek(Timestamp start, Timestamp end) {
		Calendar st = getCalendarByTimeStamp(start.getTime());
		Calendar et = getCalendarByTimeStamp(end.getTime());
		int days = Math.abs(TimeUtil.getSoFarWentDays(st, et));
		if (days < TimeUtil.DAY_OF_WEEK) {
			// 设置Monday为一周的开始
			st.setFirstDayOfWeek(Calendar.MONDAY);
			et.setFirstDayOfWeek(Calendar.MONDAY);
			return st.get(Calendar.WEEK_OF_YEAR) == et.get(Calendar.WEEK_OF_YEAR);
		}
		return false;
	}

	/**
	 * 判断start和end是否在同一个月内
	 */
	public static boolean isSameMonth(Timestamp start, Timestamp end) {
		Calendar st = getCalendarByTimeStamp(start.getTime());
		Calendar et = getCalendarByTimeStamp(end.getTime());
		return st.get(Calendar.YEAR) == et.get(Calendar.YEAR) && st.get(Calendar.MONTH) == et.get(Calendar.MONTH);
	}

	public static boolean betweenTimeStamp(Timestamp nowTimeStamp, Timestamp startTimeStamp, Timestamp endTimeStamp) {
		return nowTimeStamp.after(startTimeStamp) && nowTimeStamp.before(endTimeStamp);
	}

	/**
	 * 下个零点时间 跟使用逻辑时区有关系
	 */
	public static long nextZeroHourTime() {
		Calendar calendar = getCalendarByTimeStamp(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime() + DAY;
	}

	public static long nextHourStartTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() + HOUR;
	}

	private static long testInterval = 15 * MINUTE;

	// 15 minutes
	public static long curTestStartTime(long currentTimeInMillis) {
		return (long) (Math.floor((double) currentTimeInMillis / testInterval) * testInterval);
	}

	public static long curZeroHourTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long curHourTime(long currentTimeInMillis, int hourNum) {
		hourNum = Math.max(hourNum, 0);
		return curZeroHourTime(currentTimeInMillis) + hourNum * HOUR;
	}

	public static long curDayStartTimeDefaultZone(long currentTimeInMillis) {
		return currentTimeInMillis / DAY * DAY;
	}

	public static long curWeekStartTimeDefaultZone(long currentTimeInMillis) {
		return currentTimeInMillis / DAY_OF_WEEK * DAY_OF_WEEK;
	}

	public static long curSundayStartTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long nextMondayStartTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (calendar.getTimeInMillis() > currentTimeInMillis) {
			return calendar.getTimeInMillis();
		}
		return calendar.getTimeInMillis() + DAY * DAY_OF_WEEK;
	}

	public static long nextSundayStartTime(long currentTimeInMillis) {
		return curSundayStartTime(currentTimeInMillis) + DAY * DAY_OF_WEEK;
	}

	public static long curMonthStartTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		return month(calendar);
	}

	public static long nextMonthStartTime(long currentTimeInMillis) {
		Calendar calendar = getCalendarByTimeStamp(currentTimeInMillis);
		int month = calendar.get(Calendar.MONTH);
		if (month == calendar.getActualMaximum(Calendar.MONTH)) {
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
		} else {
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		}
		return month(calendar);
	}

	private static long month(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取数字格式当前日期 和配置的逻辑时区有关系
	 */
	public static int timestampToDateNumber(long time) {
		Calendar cal = getCalendarByTimeStamp(time);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	public static long dateNumberToTimestamp(int utcDateNumber) {
		int year = utcDateNumber / 10000;
		int month = (utcDateNumber % 10000) / 100;
		int day = utcDateNumber % 100;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(getTimeZone());
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 时间戳转文本时间 和配置的逻辑时区有关系
	 */
	public static String getCurTimeStr() {
		long curTime = System.currentTimeMillis();
		Calendar calendar = getCalendarByTimeStamp(curTime);
		return calendar.toString();
	}

	/**
	 * 获取当前星期数字 0-6 星期日开始 和配置的逻辑时区有关系
	 */
	public static int getWeekNumber(long time) {
		Calendar c = getCalendarByTimeStamp(time);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 时间戳转文本时间 和配置的逻辑时区没有关系 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getTimeStr(long time) {
		return getTimeStr(time, DATE_FORMAT1);
	}

	/**
	 * 时间戳转文本时间 和配置的逻辑时区没有关系
	 */
	public static String getTimeStr(long time, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date(time));
	}

	/**
	 * 时间戳转文本时间 和配置的逻辑时区有关系 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getLogicalTimeStr(long time) {
		return getLogicalTimeStr(time, DATE_FORMAT1);
	}

	/**
	 * 时间戳转文本时间 和配置的逻辑时区有关系
	 */
	public static String getLogicalTimeStr(long time, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(getTimeZone());
		return simpleDateFormat.format(new Date(time));
	}

	/**
	 * 获取时间的年号
	 */
	public static int getTimeYear(long timeStamp) {
		Calendar calendarByTimeStamp = getCalendarByTimeStamp(timeStamp);
		return calendarByTimeStamp.get(Calendar.YEAR);
	}

	/**
	 * 根据 time calendarNum 获取 日历参数 Calendar.YEAR  HOUR_OF_DAY
	 */
	public static int getCalendarNum(int calendarNum, long time) {
		Calendar calendar = getCalendarByTimeStamp(time);
		return calendar.get(calendarNum);
	}


	/**
	 * 根据 time  指定之后(isBefore false)之前(isBefore true) 的 Calendar.YEAR  HOUR_OF_DAY 数量时间
	 *
	 * @param time           给定的时间戳
	 * @param isBefore       之后或者之前(true是之前  false是之后)
	 * @param calendarNum    日历常量
	 * @param afterBeforeNum 之前之后的数量 几分钟 几小时 几秒钟 几年
	 * @return 改变后的时间戳
	 */
	public static long getFixTime(long time, boolean isBefore, int calendarNum, int afterBeforeNum) {
		Calendar calendar = getCalendarByTimeStamp(time);
		if (!isBefore) {
			afterBeforeNum = -afterBeforeNum;
		}
		calendar.add(calendarNum, afterBeforeNum);
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据开始时间，和单次周期分钟数，得到当前周期结束数
	 *
	 * @param startTime      开始时间戳
	 * @param minuteDuration 单次周期持续分钟
	 */
	public static long getCurEndTime(long startTime, long minuteDuration) {
		long currentTimeMillis = System.currentTimeMillis();
		long onceStageTime = minuteDuration * TimeUtil.MINUTE;
		long goingTime = currentTimeMillis - startTime;
		long stageGoingTime = goingTime % onceStageTime;
		return currentTimeMillis - stageGoingTime + onceStageTime;
	}
}
