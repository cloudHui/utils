package utils.other.gen;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json生成Java 模板类
 */
public class JsonToClassGenerator {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void generateClass(String jsonStr, String className, String packageName, String outputDir) throws Exception {
		JsonNode rootNode = mapper.readTree(jsonStr);
		StringBuilder classContent = new StringBuilder();

		// 构建包声明
		if (packageName != null && !packageName.isEmpty()) {
			classContent.append("package ").append(packageName).append(";\n\n");
		}

		// 构建类定义
		classContent.append("public class ").append(className).append(" {\n");

		// 添加字段
		Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> entry = fields.next();
			String fieldName = entry.getKey();
			JsonNode valueNode = entry.getValue();

			String fieldType = getJavaType(valueNode);
			classContent.append("    private ").append(fieldType)
					.append(" ").append(fieldName).append(";\n\n");
		}

		// 添加getter/setter
		fields = rootNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> entry = fields.next();
			String fieldName = entry.getKey();
			JsonNode valueNode = entry.getValue();
			String fieldType = getJavaType(valueNode);
			String capitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			// Getter
			classContent.append("\n    public ").append(fieldType)
					.append(" get").append(capitalized).append("() {\n")
					.append("        return this.").append(fieldName).append(";\n")
					.append("    }\n");

			// Setter
			classContent.append("\n    public void set").append(capitalized)
					.append("(").append(fieldType).append(" ").append(fieldName).append(") {\n")
					.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
					.append("    }\n");
		}

		classContent.append("}\n");

		// 写入文件
		File dir = new File(outputDir);
		if (!dir.exists()) {
			System.out.println(" not exist create success " + dir.mkdirs());
		}

		try (FileWriter writer = new FileWriter(outputDir + File.separator + className + ".java")) {
			writer.write(classContent.toString());
		}
	}

	private static String getJavaType(JsonNode node) {
		if (node.isInt()) return "int";
		if (node.isLong()) return "long";
		if (node.isDouble()) return "double";
		if (node.isBoolean()) return "boolean";
		return "String";
	}
}
