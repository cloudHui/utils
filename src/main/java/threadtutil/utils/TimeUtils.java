package threadtutil.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static int PROCESS_NUMBER = Runtime.getRuntime().availableProcessors() * 2;

	public TimeUtils() {
	}

	public static final Date defaultTime() {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1970-01-01 00:00:00");
		} catch (ParseException var1) {
			return null;
		}
	}

	public static final Date now() {
		return new Date();
	}

	public static final long time() {
		return System.currentTimeMillis();
	}

	public static final long delayTime(long time) {
		return System.currentTimeMillis() + 1000L * time;
	}

	public static final Date getDate(long mills) {
		return new Date(mills);
	}

	public static final String getDate(Date date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}

	public static final String getStrDate(long mills) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(mills));
	}

	public static final long diffTime(Date d1, Date d2) {
		return (d1.getTime() - d2.getTime()) / 1000L;
	}

	public static final Date getDelayDate(Date date, long second) {
		return new Date(date.getTime() + second * 1000L);
	}

	public static final Date getDelaySecond(Date date, int delay) {
		return getDelay(date, -1, delay);
	}

	public static final Date getDelay(Date date, int type, int delay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (type) {
			case -1:
				calendar.add(13, delay);
				break;
			case 0:
				calendar.add(12, delay);
				break;
			case 1:
				calendar.add(10, delay);
				break;
			case 2:
				calendar.add(5, delay);
				break;
			case 3:
				calendar.add(5, 7 * delay);
				break;
			case 4:
				calendar.add(2, delay);
		}

		return calendar.getTime();
	}
}
