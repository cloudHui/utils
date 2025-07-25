//package utils.other.gen.test;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.coolfish.framework.commons.log.CFLoggerFactory;
//import com.coolfish.framework.commons.log.ICFLogger;
//import com.coolfish.framework.config.FileConfig;
//import com.coolfish.framework.config.Resource;
//import com.coolfish.framework.config.ResourceConfig;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
///**
// * 生成配置表模型代码
// */
//public class GeneratorJava {
//	private static final ICFLogger logger = CFLoggerFactory.getLogger(GeneratorJava.class);
//	public static final String XML_DIR = "\\Document\\Configure\\kongzhongtest\\XML\\";
//	public static final String LAN_DIR = "\\Languages\\";
//
//	public static final String LAN_PREFIX = "languages";
//
//	public static final String resources = "\\resources\\resource.xml";
//
//	public static final String resourcesXml = "\\resources\\xml\\";
//	public static final String CF_PROJECT = "if2-configure";
//	public static final String RETURN = "\r\n";
//	public static final String CONFIG_MANAGER = "XmlConfigManager";
//	public static final String PACKAGE_PREFIX = "com.coolfish.ironforce2.configure";
//
//	// 类型映射：Excel类型 -> Java获取方法
//	private static final Map<String, String> TYPE_TO_GET_METHOD = new HashMap<>();
//
//	private static final Set<String> SERVERS = new HashSet<>();
//	private static final Set<String> LAN_SERVERS = new HashSet<>();
//
//	public static String ALL_XML;
//	public static Path AB_PATH;
//	public static Path PA_PATH;
//
//	static {
//		CFLoggerFactory.initDefault();
//		addType();
//		addServer();
//		initPath();
//	}
//
//	private static void addType() {
//		TYPE_TO_GET_METHOD.put("float", "getFloat");
//		TYPE_TO_GET_METHOD.put("int", "getInt");
//		TYPE_TO_GET_METHOD.put("long", "getLong");
//		TYPE_TO_GET_METHOD.put("String", "getString");
//		TYPE_TO_GET_METHOD.put("boolean", "getBoolean");
//		TYPE_TO_GET_METHOD.put("Vector2", "getVector2");
//		TYPE_TO_GET_METHOD.put("Vector3", "getVector3");
//		TYPE_TO_GET_METHOD.put("Timestamp", "getTimestamp");
//		TYPE_TO_GET_METHOD.put("String[]", "getStringArray");
//		TYPE_TO_GET_METHOD.put("int[]", "getIntArray");
//		TYPE_TO_GET_METHOD.put("int[][]", "getIntArrayArray");
//		TYPE_TO_GET_METHOD.put("long[]", "getLongArray");
//		TYPE_TO_GET_METHOD.put("float[]", "getFloatArray");
//		TYPE_TO_GET_METHOD.put("float[][]", "getFloatArrayArray");
//		TYPE_TO_GET_METHOD.put("Vector2[]", "getVector2Array");
//		TYPE_TO_GET_METHOD.put("Vector3[]", "getVector3Array");
//		TYPE_TO_GET_METHOD.put("IntFloat[]", "getIntFloatArray");
//		TYPE_TO_GET_METHOD.put("Timestamp[]", "getTimestampArray");
//	}
//
//	private static void addServer() {
//		SERVERS.add("if2-battleManagerServer");
//		SERVERS.add("if2-gameServer");
//		SERVERS.add("if2-gatewayServer");
//		SERVERS.add("if2-gmOrderServer");
//		SERVERS.add("if2-httpServer");
//		SERVERS.add("if2-notificServer");
//		SERVERS.add("if2-pressureTestServer");
//		SERVERS.add("if2-routerServer");
//		SERVERS.add("if2-socialServer");
//		SERVERS.add("if2-battleServer");
//
//		LAN_SERVERS.add("if2-gameServer");
//		LAN_SERVERS.add("if2-httpServer");
//		LAN_SERVERS.add("if2-notificServer");
//	}
//
//	private static void initPath() {
//		AB_PATH = Paths.get("").toAbsolutePath();
//		PA_PATH = AB_PATH.getParent();
//		ALL_XML = PA_PATH + XML_DIR;
//	}
//
//	public static Set<String> getSERVERS() {
//		return SERVERS;
//	}
//
//	public static Set<String> getLanServers() {
//		return LAN_SERVERS;
//	}
//
//	// 获取基础路径
//	private static Path getBasePath() {
//		String name = GeneratorJava.class.getName().replace(".", File.separator);
//		name = name.substring(0, name.lastIndexOf(File.separator));
//		name = name.substring(0, name.lastIndexOf(File.separator));
//		name = name.substring(0, name.lastIndexOf(File.separator));
//		return Paths.get(AB_PATH + File.separator + CF_PROJECT + File.separator +
//				"src" + File.separator + "main" + File.separator + "java" +
//				File.separator + name);
//	}
//
//	/**
//	 * 生成Java 代码
//	 */
//	public static void generateJavaCode(String param) {
//		try {
//			logger.error("generateJavaCode path :{}", ALL_XML);
//			// 遍历所有Excel文件并生成Java代码
//			for (File excelFile : getAllXmlFiles(ALL_XML, ".xlsx", param).values()) {
//				if (excelFile != null) {
//					generateJavaFromExcel(excelFile);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("生成过程异常", e);
//		}
//	}
//
//	public static Set<String> getUseTable(String param) {
//		Set<String> tableName = new HashSet<>();
//		for (String server : SERVERS) {
//			String resourcePath = AB_PATH + File.separator + server + resources;
//			File file = new File(resourcePath);
//			if (!file.exists()) {
//				logger.error("{} 没有这个resources 文件 ", file.getName());
//				return tableName;
//			}
//			try {
//				ResourceConfig config = Resource.load(file);
//				List<FileConfig> fileConfigList = config.getFileConfigList();
//				for (FileConfig fileConfig : fileConfigList) {
//					tableName.add(fileConfig.getName());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return tableName;
//	}
//
//	// ================ 核心生成逻辑 ================
//	private static void generateJavaFromExcel(File excelFile) {
//		try (InputStream is = new FileInputStream(excelFile);
//				XSSFWorkbook workbook = new XSSFWorkbook(is)) {
//
//			String fileName = excelFile.getName();
//			if (fileName.contains("TableConst")) {
//				// 常量表：处理所有sheet
//				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//					generateConstClass(workbook.getSheetAt(i));
//				}
//			} else {
//				// 数据表：只处理第一个sheet
//				generateModelClass(fileName, workbook.getSheetAt(0));
//			}
//		} catch (Exception e) {
//			logger.error("处理文件失败: " + excelFile.getName(), e);
//		}
//	}
//
//	private static void generateModelClass(String fileName, XSSFSheet sheet) {
//		String className = fileName.split("\\.")[0];
//		List<ClassMember> members = parseSheetMembers(sheet);
//		String javaCode = buildModelClassCode(className, members);
//
//		// 写入文件
//		Path outputPath = Paths.get(getBasePath() + File.separator + className + ".java");
//		writeJavaFile(outputPath, javaCode);
//	}
//
//	private static void generateConstClass(XSSFSheet sheet) {
//		String className = sheet.getSheetName().split("\\.")[0];
//		StringBuilder javaCode = new StringBuilder();
//		StringBuilder imports = new StringBuilder();
//		boolean requiresUtils = false;
//
//		// 遍历行生成常量方法
//		for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
//			XSSFRow row = sheet.getRow(i);
//			if (row == null)
//				continue;
//
//			// 检查是否启用
//			String needPass = ExcelToXmlUtil.getCellData(row.getCell(6));
//			if ((int) Double.parseDouble(needPass) != 1) {
//				continue;
//			}
//			// 解析单元格数据
//			int id = (int) Double.parseDouble(ExcelToXmlUtil.getCellData(row.getCell(0)));
//			String key = cleanKey(ExcelToXmlUtil.getCellData(row.getCell(1)));
//			String type = convertType(ExcelToXmlUtil.getCellData(row.getCell(2)));
//			String value = getConstTypeValue(type, row.getCell(3), className, i, key);
//			String desc = ExcelToXmlUtil.getCellData(row.getCell(4));
//
//			// 生成方法代码
//			MethodGeneratorResult result = generateConstMethod(id, key, type, value, desc);
//			javaCode.append(result.code);
//			requiresUtils |= result.requiresUtils;
//
//			// 收集需要导入的类型
//			getCommonImport(imports, type);
//		}
//
//		if (javaCode.length() > 0) {
//			String fullCode = buildConstClassHeader(className, imports.toString(), requiresUtils) + javaCode + "}\n";
//			// 文件名绝对路径
//			Path outputPath = Paths.get(getBasePath() + File.separator + "consts" + File.separator + className + ".java");
//			writeJavaFile(outputPath, fullCode);
//		}
//	}
//
//	// ================ 工具方法 ================
//	private static MethodGeneratorResult generateConstMethod(int id, String key, String type, String value, String desc) {
//		String methodName = "get" + capitalize(key);
//		String returnType = convertType(type);
//
//		StringBuilder code = new StringBuilder()
//				.append("\n\t/**\n\t * ").append(desc).append(" (").append(getConstValueDesc(type, value)).append(")\n\t */\n")
//				.append("\tpublic ").append(returnType).append(" ").append(methodName).append("() {\n\t\treturn ");
//
//		// 添加类型转换逻辑
//		TypeParserInfo parserInfo = getTypeParser(type);
//		code.append(parserInfo.parsePrefix).append("(" + CONFIG_MANAGER + ".getInstance().getConstTable(").append(id).append(")).getValue()")
//				.append(parserInfo.parseSuffix)
//				.append(";\n\t}\n");
//
//		return new MethodGeneratorResult(code.toString(), parserInfo.requiresUtils);
//	}
//
//	private static String getConstTypeValue(String type, XSSFCell cell, String className, int row, String key) {
//		try {
//			switch (type) {
//				case "int":
//				case "long":
//				case "float":
//					return getConstValueDesc(type, cell.getNumericCellValue() + "");
//				default:
//					return ExcelToXmlUtil.getCellData(cell);
//			}
//		} catch (Exception e) {
//			logger.error("getNumericValue error sheet:{} row:{} cell:{} type:{} key:{}", className, row, cell, type, key);
//			try {
//				return getConstValueDesc(type, cell.getStringCellValue() + "");
//			} catch (Exception e1) {
//				logger.error("getConstTypeValue  again sheet:{} row:{} cell:{} type:{} key:{}", className, row, cell, type, key, e);
//			}
//		}
//		return "";
//	}
//
//	private static String getConstValueDesc(String type, String value) {
//		switch (type) {
//			case "int":
//				return String.valueOf((int) Double.parseDouble(value));
//			case "float":
//				return Double.parseDouble(value) + "";
//			case "long":
//				return String.valueOf((long) Double.parseDouble(value));
//			default:
//				return value;
//		}
//	}
//
//	private static TypeParserInfo getTypeParser(String type) {
//		TypeParserInfo info = new TypeParserInfo();
//		switch (type) {
//			case "bool":
//			case "boolean":
//				info.parsePrefix = "Boolean.parseBoolean(";
//				break;
//			case "int":
//				info.parsePrefix = "Integer.parseInt(";
//				break;
//			case "long":
//				info.parsePrefix = "Long.parseLong(";
//				break;
//			case "float":
//				info.parsePrefix = "Float.parseFloat(";
//				break;
//			case "Vector2":
//				info.parsePrefix = "TableUtils.Vector2Parse(";
//				info.requiresUtils = true;
//				break;
//			case "Vector3":
//				info.parsePrefix = "TableUtils.Vector3Parse(";
//				info.requiresUtils = true;
//				break;
//			case "Timestamp":
//				info.parsePrefix = "TableUtils.TimeStampParse(";
//				info.requiresUtils = true;
//				break;
//			case "int[]":
//				info.parsePrefix = "TableUtils.IntArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "float[]":
//				info.parsePrefix = "TableUtils.FloatArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "String[]":
//				info.parsePrefix = "TableUtils.StringArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "Vector2[]":
//				info.parsePrefix = "TableUtils.Vector2ArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "Vector3[]":
//				info.parsePrefix = "TableUtils.Vector3ArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "Timestamp[]":
//				info.parsePrefix = "TableUtils.TimestampArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "int[][]":
//				info.parsePrefix = "TableUtils.IntArrayArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "String[][]":
//				info.parsePrefix = "TableUtils.StringArrayArrayParse(";
//				info.requiresUtils = true;
//				break;
//			case "float[][]":
//				info.parsePrefix = "TableUtils.FloatArrayArrayParse(";
//				info.requiresUtils = true;
//				break;
//			default:
//				info.parsePrefix = "";
//		}
//
//		// 设置后缀
//		info.parseSuffix = info.parsePrefix.isEmpty() ? "" : ")";
//		return info;
//	}
//
//	private static String buildModelClassCode(String className, List<ClassMember> members) {
//		StringBuilder imports = new StringBuilder();
//		StringBuilder fields = new StringBuilder();
//		StringBuilder constructors = new StringBuilder();
//		StringBuilder gettersSetters = new StringBuilder(RETURN);
//
//		// 包声明和基础导入
//		StringBuilder code = new StringBuilder()
//				.append("package ").append(PACKAGE_PREFIX).append(";").append(RETURN).append(RETURN)
//				.append("import java.util.HashMap;").append(RETURN)
//				.append("import com.alibaba.fastjson.annotation.JSONField;").append(RETURN)
//				.append("import com.coolfish.framework.commons.utils.TableUtils;").append(RETURN);
//
//		// 处理每个成员
//		for (ClassMember member : members) {
//			// 跳过注释字段
//			if (member.name.contains("$$"))
//				continue;
//
//			String cleanName = cleanKey(member.name);
//			String javaType = convertType(member.type);
//
//			// 收集特殊类型导入
//			getCommonImport(imports, javaType);
//
//			// 构建字段声明
//			fields.append("\t@JSONField(name = \"").append(cleanName).append("\") ").append(RETURN)
//					.append("\tprivate ").append(javaType).append(" ").append(cleanName).append(";")
//					.append(RETURN).append(RETURN);
//
//			// 构建构造方法内容
//			if (TYPE_TO_GET_METHOD.containsKey(javaType)) {
//				constructors.append("\t\tthis.").append(cleanName).append(" = TableUtils.")
//						.append(TYPE_TO_GET_METHOD.get(javaType)).append("(map, \"")
//						.append(cleanName).append("\");").append(RETURN);
//			}
//
//			// 构建getter/setter
//			String capitalized = capitalize(cleanName);
//
//			member.description = member.description.replace("\n", ";");
//			gettersSetters.append("\t/**" + RETURN + "\t * ").append(member.description).append(RETURN + "\t */" + RETURN);
//			gettersSetters.append("\tpublic ").append(javaType).append(" get").append(capitalized).append("() {")
//					.append(RETURN).append("\t\treturn this.").append(cleanName).append(";")
//					.append(RETURN).append("\t}").append(RETURN).append(RETURN)
//					.append("\tpublic void set").append(capitalized).append("(").append(javaType)
//					.append(" ").append(cleanName).append(") {").append(RETURN)
//					.append("\t\tthis.").append(cleanName).append(" = ").append(cleanName).append(";")
//					.append(RETURN).append("\t}").append(RETURN).append(RETURN);
//		}
//
//		// 添加特殊类型导入
//		code.append(imports).append(RETURN);
//
//		// 类声明
//		code.append("public class ").append(className).append(" {").append(RETURN);
//
//		// 构造方法
//		code.append("\tpublic ").append(className).append("(HashMap<String, Object> map) {").append(RETURN)
//				.append(constructors)
//				.append("\t}").append(RETURN).append(RETURN);
//
//		// 无参构造
//		code.append("\tpublic ").append(className).append("() {").append(RETURN).append("\t}").append(RETURN).append(RETURN);
//
//		// 字段和方法
//		code.append(fields).append(gettersSetters).append("}").append(RETURN);
//		return code.toString();
//	}
//
//	private static void getCommonImport(StringBuilder imports, String javaType) {
//		if (javaType.contains("Vector2") && !imports.toString().contains("Vector2")) {
//			imports.append("import com.coolfish.framework.commons.utils.math.Vector2;").append(RETURN);
//		} else if (javaType.contains("Vector3") && !imports.toString().contains("Vector3")) {
//			imports.append("import com.coolfish.framework.commons.utils.math.Vector3;").append(RETURN);
//		} else if (javaType.contains("IntFloat") && !imports.toString().contains("IntFloat")) {
//			imports.append("import com.coolfish.framework.commons.utils.math.IntFloat;").append(RETURN);
//		} else if (javaType.contains("Timestamp") && !imports.toString().contains("Timestamp")) {
//			imports.append("import java.sql.Timestamp;").append("\n");
//		}
//	}
//
//	private static String buildConstClassHeader(String className, String imports, boolean requiresUtils) {
//		StringBuilder header = new StringBuilder()
//				.append("package ").append(PACKAGE_PREFIX).append(".consts;").append(RETURN).append(RETURN)
//				.append("import ").append(PACKAGE_PREFIX).append(".manager.XmlConfigManager;").append(RETURN);
//
//		if (requiresUtils) {
//			header.append("import com.coolfish.framework.commons.utils.TableUtils;").append(RETURN);
//		}
//
//		header.append(imports).append(RETURN)
//				.append("public class ").append(className).append(" {").append(RETURN)
//				.append("\tprivate static final ").append(className).append(" instance = new ").append(className).append("();").append(RETURN).append(RETURN)
//				.append("\tprivate ").append(className).append("() {\n\t}").append(RETURN).append(RETURN)
//				.append("\tpublic static ").append(className).append(" getInstance() {").append(RETURN)
//				.append("\t\treturn instance;").append(RETURN)
//				.append("\t}").append(RETURN).append(RETURN);
//
//		return header.toString();
//	}
//
//	// ================ 辅助方法 ================
//	public static Map<String, File> getAllXmlFiles(String dirPath, String suffix, String param) {
//		Map<String, File> fileMap = new HashMap<>();
//		File dir = new File(dirPath);
//		File[] files = dir.listFiles();
//		if (files != null) {
//			String fileName;
//			for (File file : files) {
//				if (file.isFile()) {
//					fileName = file.getName();
//					if (!fileName.endsWith(suffix)) {
//						continue;
//					}
//					if (param.equals("-in") && fileName.contains(".CN.")) {
//						continue;
//					} else if (param.equals("-cn") && fileName.contains(".IN.")) {
//						continue;
//					}
//					fileMap.put(fileName, file);
//				}
//			}
//		}
//		return fileMap;
//	}
//
//	private static List<ClassMember> parseSheetMembers(XSSFSheet sheet) {
//		List<ClassMember> members = new ArrayList<>();
//		if (sheet.getPhysicalNumberOfRows() < 3)
//			return members;
//
//		// 前三行：名称、类型、描述
//		XSSFRow nameRow = sheet.getRow(0);
//		XSSFRow typeRow = sheet.getRow(1);
//		XSSFRow descRow = sheet.getRow(2);
//
//		for (int i = 0; i < nameRow.getPhysicalNumberOfCells(); i++) {
//			String name = ExcelToXmlUtil.getCellData(nameRow.getCell(i));
//			String type = ExcelToXmlUtil.getCellData(typeRow.getCell(i));
//			String desc = ExcelToXmlUtil.getCellData(descRow.getCell(i));
//			members.add(new ClassMember(name, type, desc));
//		}
//		return members;
//	}
//
//	private static void writeJavaFile(Path path, String content) {
//		try {
//			Files.createDirectories(path.getParent());
//			try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//				writer.write(content);
//				logger.error("生成Java文件成功: " + path.getFileName());
//			}
//		} catch (IOException e) {
//			logger.error("文件写入失败: " + path, e);
//		}
//	}
//
//	private static String convertType(String type) {
//		if (type == null)
//			return "String";
//		if (type.equals("bool"))
//			return type.replace("bool", "boolean");
//		if (type.contains("string"))
//			return type.replace("string", "String");
//		return type;
//	}
//
//	private static String cleanKey(String key) {
//		return key.replace("$", "");
//	}
//
//	private static String capitalize(String str) {
//		if (str == null || str.isEmpty())
//			return str;
//		return str.substring(0, 1).toUpperCase() + str.substring(1);
//	}
//
//	// ================ 辅助类 ================
//	static class ClassMember {
//		String name;
//		String type;
//		String description;
//
//		public ClassMember(String name, String type, String description) {
//			this.name = name;
//			this.type = type;
//			this.description = description;
//		}
//	}
//
//	static class MethodGeneratorResult {
//		String code;
//		boolean requiresUtils;
//
//		public MethodGeneratorResult(String code, boolean requiresUtils) {
//			this.code = code;
//			this.requiresUtils = requiresUtils;
//		}
//	}
//
//	static class TypeParserInfo {
//		String parsePrefix = "";
//		String parseSuffix = "";
//		boolean requiresUtils = false;
//	}
//}