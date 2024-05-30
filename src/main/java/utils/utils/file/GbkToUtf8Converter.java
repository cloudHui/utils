package utils.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GbkToUtf8Converter {
	public static void main(String[] args) {
		String sourceFilePath = "神秘之劫.txt"; // GBK编码的源文件路径
		String targetFilePath = "resources/神秘之劫.txt"; // 转换后UTF-8编码的目标文件路径

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GbkToUtf8Converter.class.getClassLoader()
				.getResourceAsStream(sourceFilePath)), "GBK"));
			 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine(); // 写入换行符，保持文件格式
			}
			System.out.println("文件转换完成。");
		} catch (IOException e) {
			System.err.println("转换文件时发生错误：" + e.getMessage());
		}
	}
}