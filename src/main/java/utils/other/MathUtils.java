package utils.other;

/**
 * @author admin
 * @className MathUtils
 * @description
 * @createDate 2025/7/9 14:38
 */
public class MathUtils {

	/**
	 * 获取后面的数
	 *
	 * @param itemNum 原数
	 * @param num     后几位 要保留的位数
	 * @return itemNum 的后 4 位数
	 */
	public static int getBehind(int itemNum, int num) {
		int base = 10;
		int divide = 1;
		for (int index = 0; index < num; index++) {
			divide *= base;
		}
		return itemNum - itemNum / divide * divide;
	}
}
