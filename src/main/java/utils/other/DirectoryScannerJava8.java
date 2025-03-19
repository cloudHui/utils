package utils.other;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class DirectoryScannerJava8 {
	public static void listFilesRecursively(String directoryPath, Map<String, Integer> path) {
		try {
			Files.walk(Paths.get(directoryPath))
					.filter(Files::isRegularFile)
					.forEach(filePath -> path.put(filePath.toAbsolutePath().toString(), 1));

			//// 若要以列表形式收集所有文件路径：
			//List<Path> fileList = Files.walk(Paths.get(directoryPath))
			//		.filter(Files::isRegularFile)
			//		.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}