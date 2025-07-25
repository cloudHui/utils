package utils.other.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 测试生成
 */
public class TestGen {
	/**
	 * 读取文件
	 */
	public static String readFile(String strFile) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(strFile);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String strLine;
			while (null != (strLine = bufferedReader.readLine())) {
				sb.append(strLine);
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static JSONObject read(String fullPath) {
		try {
			String content = readFile(fullPath);
			return JSON.parseObject(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String fullPath = System.getProperty("user.dir") + File.separator + "resources\\" + "config.json";
		String json = readFile(fullPath);
		try {
			JsonToClassGenerator.generateClass(
					json,
					"Config",
					"utils.other.gen",
					"src/main/java/utils/other/gen"
			);
			System.out.println("Java类文件生成成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
