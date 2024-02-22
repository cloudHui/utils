package utils.utils;

import java.io.File;

public class FileChecker {
	public static long lastModifiedTime = System.currentTimeMillis(); // 记录上次扫描的最后修改时间

	public static boolean hasNewFiles(String directoryPath) {
		File dir = new File(directoryPath);

		if (!dir.exists() || !dir.isDirectory()) {
			return false; // 若路径不存在或者不是目录，直接返回false
		}

		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		for (File file : files) {
			if (file.lastModified() > lastModifiedTime) {
				return true; // 如果发现有文件被修改过（包括创建），则认为有新增文件
			}
		}

		return false; // 没有新增文件
	}
}