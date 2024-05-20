package utils.utils;

/**
 * 数字转换工具类
 */
public final class NumberUtil {

	/**
	 * 将字符串转化成int，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static int stringToNumber(String str, int defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(str); // 自动拆箱
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成int，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static int stringToNumber(Object str, int defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return stringToNumber(String.valueOf(str), defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成int，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static double stringToNumber(Object str, double defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return stringToNumber(String.valueOf(str), defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成Integer，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static Integer stringToNumber(String str, Integer defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成int，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static long stringToNumber(Object str, long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return stringToNumber(String.valueOf(str), defaultValue); // 自动拆箱
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成long，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static long stringToNumber(String str, long defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Long.parseLong(str); // 自动拆箱
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成Long，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static Long stringToNumber(String str, Long defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成double，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static double stringToNumber(String str, double defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(str); // 自动拆箱
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成Double，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static Double stringToNumber(String str, Double defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成float，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static float stringToNumber(String str, float defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(str); // 自动拆箱
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 将字符串转化成Float，如果字符串为空字符串或者为空，则返回 默认值
	 *
	 * @param str          要被转化的字符串
	 * @param defaultValue 默认值
	 */
	public static Float stringToNumber(String str, Float defaultValue) {
		if (str == null || str.length() == 0) {
			return defaultValue;
		}
		try {
			return Float.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}


}
