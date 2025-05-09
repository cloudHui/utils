package utils.other;

import java.io.File;
import java.util.Map;

public class DirectoryScanner {


	public static void listFilesInDirectory(String directoryPath, Map<String, Integer> path) {
		File directory = new File(directoryPath);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						path.put(file.getAbsolutePath(), 1);
					} else if (file.isDirectory()) {
						// 如果你想递归获取子目录中的文件，可以在這裡添加遞歸调用
						listFilesInDirectory(file.getAbsolutePath(), path);
					}
				}
			}
		} else {
			System.out.println("The provided path is not a directory or does not exist.");
		}
	}

	/**
	 * 加载文件
	 *
	 * @param directoryPath 路径
	 * @param fileMap       存成 文件和修改时间是为了 map get 直接取相同的值方便
	 */
	public static void listFiles(String directoryPath, Map<String, Long> fileMap) {
		File directory = new File(directoryPath);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						fileMap.put(file.getName(), file.lastModified());
					} else if (file.isDirectory()) {
						// 如果你想递归获取子目录中的文件，可以在這裡添加遞歸调用
						listFiles(file.getAbsolutePath(), fileMap);
					}
				}
			}
		} else {
			throw new RuntimeException("no file");
		}
	}
}
