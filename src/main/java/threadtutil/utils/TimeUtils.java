package threadtutil.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	public final static int SECOND = 1;
	public final static int MINUTE = 2;
	public final static int HOUR = 3;
	public final static int DATE = 4;
	public final static int WEEK = 5;
	public final static int MONTH = 6;
	public final static int YEAR = 7;

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static int PROCESS_NUMBER = Runtime.getRuntime().availableProcessors() * 2;

	public TimeUtils() {
	}

	public static Date defaultTime() {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 00:00:00");
		} catch (ParseException var1) {
			return null;
		}
	}

	public static Date now() {
		return new Date();
	}

	public static long time() {
		return System.currentTimeMillis();
	}

	public static long delayTime(long time) {
		return System.currentTimeMillis() + 1000L * time;
	}

	public static Date getDate(long mills) {
		return new Date(mills);
	}

	public static String getDate(Date date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}

	public static String getStrDate(long mills) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(mills));
	}

	public static long diffTime(Date d1, Date d2) {
		return (d1.getTime() - d2.getTime()) / 1000L;
	}

	public static Date getDelayDate(Date date, long second) {
		return new Date(date.getTime() + second * 1000L);
	}

	public static Date getDelaySecond(Date date, int delay) {
		return getDelay(date, SECOND, delay);
	}

	/**
	 * @param date  需要延迟的起始时间
	 * @param type  延迟类型
	 *              SECOND = 1;
	 *              MINUTE = 2;
	 *              HOUR = 3;
	 *              DATE = 4;
	 *              MONTH = 5;
	 *              YEAR = 6;
	 * @param delay 延迟的数量
	 * @return 延迟后的时间
	 */
	public static Date getDelay(Date date, int type, int delay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (type) {
			case SECOND:
				calendar.add(Calendar.SECOND, delay);
				break;
			case MINUTE:
				calendar.add(Calendar.MINUTE, delay);
				break;
			case HOUR:
				calendar.add(Calendar.HOUR, delay);
				break;
			case DATE:
				calendar.add(Calendar.DATE, delay);
				break;
			case WEEK:
				calendar.add(Calendar.DATE, delay * 7);
				break;
			case MONTH:
				calendar.add(Calendar.MONTH, delay);
				break;
			case YEAR:
				calendar.add(Calendar.YEAR, delay);
			default:
				break;
		}

		return calendar.getTime();
	}
}
