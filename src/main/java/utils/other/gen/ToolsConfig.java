package utils.other.gen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolsConfig{

	private static final Logger logger = LoggerFactory.getLogger(ToolsConfig.class);
	private static ToolsConfig instance;

	private static final String BASE_BACK = "baseBack";

	private static final String SERVERS = "servers";

	private static final String LAN_SEVER = "lanSever";

	private static final String RESOURCE_CON_XML = "resourceConXml";

	private static final String RESOURCE_DIR = "resourceDir";

	private static final String EXCEL_DIR = "excelDir";

	private static final String LAN_CON_XLSX = "lanConXlsx";

	JSONObject jsonObject = JSON.parseObject("");

	protected String getStringFromJson(String key) {
		String result = "";
		try {
			result = jsonObject.getString(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	public String getBaseBack() {
		return getStringFromJson(BASE_BACK);
	}

	public String getServers() {
		return getStringFromJson(SERVERS);
	}

	public String getLanSever() {
		return getStringFromJson(LAN_SEVER);
	}

	public String getResourceConXml() {
		return getStringFromJson(RESOURCE_CON_XML);
	}

	public String getResourceDir() {
		return getStringFromJson(RESOURCE_DIR);
	}

	public String getExcelDir() {
		return getStringFromJson(EXCEL_DIR);
	}

	public String getLanConXlsx() {
		return getStringFromJson(LAN_CON_XLSX);
	}

	@Gen
	private String baseBack;
	@Gen
	private List<String> servers;
	@Gen
	private List<String> lanSever;
	@Gen
	private String resourceConXml;
	@Gen
	private String resourceDir;
	@Gen
	private String excelDir;
	@Gen
	private String lanConXlsx;

	public static ToolsConfig getInstance() {
		if (instance == null) {
			instance = new ToolsConfig();
		}
		return instance;
	}

	public static void main(String[] args) {
		Field[] fields = ToolsConfig.class.getDeclaredFields();
		String fileName;

		List<String> fileNameBig = new ArrayList<>();
		// 遍历字段
		for (Field field : fields) {
			// 检查字段是否带有指定注解
			if (field.isAnnotationPresent(Gen.class)) {
				// 获取注解实例
				fileName = field.getName();
				String variableName = convertToVariableName(fileName);
				System.out.println("private static final String " + variableName + " = \"" + fileName + "\";");
				System.out.println();
				fileNameBig.add(fileName + "-" + variableName);
			}
		}

		for (String value : fileNameBig) {
			String[] split = value.split("-");
			fileName = split[0];
			String variableName = split[1];
			System.out.println("public String get" + fileName.substring(0, 1).toUpperCase() + fileName.substring(1) + "() {");
			System.out.println("\t return getStringFromJson(" + variableName + ");");
			System.out.println("}");
			System.out.println();
		}
	}

	public static String convertToVariableName(String str) {
		// 将字符串转为大写并添加下划线分隔
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append('_');
			}
			sb.append(Character.toUpperCase(c));
		}
		return sb.toString();
	}
}
