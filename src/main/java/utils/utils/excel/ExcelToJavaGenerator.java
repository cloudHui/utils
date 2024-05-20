package utils.utils.excel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import utils.utils.TableUtils;

public class ExcelToJavaGenerator {


	private static void doWrite(String javaName, String path, String content) {
		String subDirectoryName = path + "/src/main/java/model"; // 子目录名称
		String fileName = javaName + ".java"; // 文件名
		Path directoryPath = Paths.get(subDirectoryName);

		// 检查子目录是否存在，如果不存在则创建
		try {
			if (!Files.exists(directoryPath)) {
				Files.createDirectories(directoryPath);
				System.out.println("子目录已创建: " + subDirectoryName);
			}
		} catch (IOException e) {
			System.err.println("创建子目录时发生错误: " + e.getMessage());
			return;
		}
		// 构建完整文件路径
		Path filePath = directoryPath.resolve(fileName);
		// 检查文件是否存在，存在则删除
		try {
			if (Files.exists(filePath)) {
				Files.delete(filePath);
				System.out.println("已删除原有文件: " + filePath);
			}
		} catch (IOException e) {
			System.err.println("删除原有文件时发生错误: " + e.getMessage());
			return;
		}

		// 尝试写入文件
		try {
			Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
			System.out.println("文件写入成功: " + filePath);
		} catch (IOException e) {
			System.err.println("写入文件时发生错误: " + e.getMessage());
		}
	}

	/**
	 * 写成java 文件
	 */
	public static void write(String javaName, String path, List<Title> titleList) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("package model;");
		sb.append(" \n").append(" \n").append(" \n");
		sb.append("public class ").append(javaName).append(" {\n");

		for (Title title : titleList) {
			addProperty(sb, title);
		}
		sb.append(" \n    // Getters and Setters\n");
		for (Title title : titleList) {
			addSetGet(sb, title);
		}
		sb.append(" }\n");
		// 写入到Java文件
		doWrite(javaName, path, sb.toString());
		System.out.println(javaName + ".java 文件已生成。");
	}

	public static String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	private static void addProperty(StringBuilder javaCode, Title title) {
		String propertyName = title.getName();
		String propertyType = title.getType();
		String type = getTypeName(propertyType);
		String desc = title.getDes();
		javaCode.append("\n");
		javaCode.append("    //").append(desc).append("\n");
		javaCode.append("    private ").append(type).append(" ").append(propertyName).append(";\n");
	}

	private static void addSetGet(StringBuilder javaCode, Title title) {
		String propertyName = title.getName();
		String propertyType = title.getType();
		String type = getTypeName(propertyType);
		javaCode.append("    public ").append(type).append(" get").append(capitalize(propertyName)).append("() {\n");
		javaCode.append("        return ").append(propertyName).append(";\n");
		javaCode.append("    }\n");
		javaCode.append("\n");
		javaCode.append("    public void set").append(capitalize(propertyName)).append("(").append(type).append(" ").append(propertyName).append(") {\n");
		javaCode.append("        this.").append(propertyName).append(" = ").append(propertyName).append(";\n");
		javaCode.append("    }\n");
		javaCode.append("\n");
	}

	private static String getTypeName(String type) {
		switch (type) {
			case "string":
				return "String";
			case "bool":
				return "booelan";
			//case "Vector2": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector2Parse(value);
			//	else
			//		obj = new Vector2();
			//}
			//break;
			//case "Vector3": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector3Parse(value);
			//	else
			//		obj = new Vector3();
			//}
			//break;
			//case "Timestamp": {
			//	if (!"".equals(value))
			//		obj = TableUtils.TimeStampParse(value);
			//	else
			//		obj = new Timestamp(0);
			//}
			//break;
			//case "string[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.StringArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			//case "Vector2[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector2ArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			//case "Vector3[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector3ArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			default:
				return type;
		}
	}


	private Object getType(String type, String value) throws Exception {
		Object obj = null;
		switch (type) {
			case "float": {
				if (!"".equals(value))
					obj = Float.parseFloat(value);
				else
					obj = 0.0F;
			}
			break;
			case "int": {
				if (!"".equals(value))
					obj = Integer.parseInt(value);
				else
					obj = 0;
			}
			break;
			case "long": {
				if (!"".equals(value))
					obj = Long.parseLong(value);
				else
					obj = 0L;
			}
			break;
			case "string": {
				obj = value;
			}
			break;
			case "bool": {
				if (!"".equals(value))
					obj = Boolean.parseBoolean(value);
				else
					obj = Boolean.FALSE;
			}
			break;
			//case "Vector2": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector2Parse(value);
			//	else
			//		obj = new Vector2();
			//}
			//break;
			//case "Vector3": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector3Parse(value);
			//	else
			//		obj = new Vector3();
			//}
			//break;
			//case "Timestamp": {
			//	if (!"".equals(value))
			//		obj = TableUtils.TimeStampParse(value);
			//	else
			//		obj = new Timestamp(0);
			//}
			//break;
			//case "string[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.StringArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			case "int[]": {
				if (!"".equals(value))
					obj = TableUtils.IntArrayParse(value);
				else
					obj = null;
			}
			break;
			case "long[]": {
				if (!"".equals(value))
					obj = TableUtils.LongArrayParse(value);
				else
					obj = null;
			}
			break;
			case "float[]": {
				if (!"".equals(value))
					obj = TableUtils.FloatArrayParse(value);
				else
					obj = null;
			}
			break;
			//case "Vector2[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector2ArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			//case "Vector3[]": {
			//	if (!"".equals(value))
			//		obj = TableUtils.Vector3ArrayParse(value);
			//	else
			//		obj = null;
			//}
			//break;
			case "Timestamp[]": {
				if (!"".equals(value) && !"[]".equals(value))
					obj = TableUtils.TimestampArrayParse(value);
				else
					obj = null;
			}
			break;
			case "int[][]": {
				if (!"".equals(value) && !"[]".equals(value))
					obj = TableUtils.IntArrayArrayParse(value);
				else
					obj = null;
			}
			break;
			case "float[][]": {
				if (!"".equals(value) && !"[]".equals(value))
					obj = TableUtils.FloatArrayArrayParse(value);
				else
					obj = null;
			}
			break;
			default:
				throw new Exception();
		}
		return obj;
	}
}