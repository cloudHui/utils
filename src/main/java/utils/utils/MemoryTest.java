package utils.utils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class MemoryTest {
	public static void main(String[] args) {
		try {
			int index = 10;
			Map<Integer, int[]> test = new HashMap<>();
			while (true) {
				if (--index > 0) {
					int[] memory = new int[1024 * 1024 * 150];
					test.put(index, memory);
				}
				Thread.sleep(1000);
				System.out.println(new Timestamp(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
