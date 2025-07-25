//package utils.other.gen.test;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import com.coolfish.framework.commons.log.CFLoggerFactory;
//import com.coolfish.framework.commons.log.ICFLogger;
//import com.coolfish.framework.commons.utils.TableUtils;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.dom.DOMElement;
//import org.dom4j.io.SAXReader;
//import org.dom4j.tree.DefaultAttribute;
//
///**
// * @author admin
// * @className Test
// * @description 测试原 xml 生成xlsx 检测数据 生成模型 和数据并复制
// * @createDate 2025/7/24 2:45
// */
//public class XmlTools {
//	private static final ICFLogger logger = CFLoggerFactory.getLogger(XmlTools.class);
//
//	static {
//		CFLoggerFactory.initDefault();
//	}
//
//	public static void main(String[] args) {
//		String param = "-in";
//		if (args != null && args.length > 0) {
//			if (args[0].equals("-cn")) {
//				param = args[0];
//			} else if (args[0].equals("-in")) {
//				param = args[0];
//			}
//		}
//		init(param);
//	}
//
//	private static void init(String param) {
//		com.coolfish.ironforce2.configure.utils.paraser.OldXmlToXlsx.generateXmlToXlsx(param);
//		com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.generateJavaCode(param);
//		excelToXmlUtil(param);
//		clearTempFolder();
//	}
//
//	private static void excelToXmlUtil(String param) {
//		CFLoggerFactory.initDefault();
//		param = param == null || param.equals("") ? "-in" : param;
//
//		boolean result = checkAllExcelFile(param);
//		if (!result) {
//			logger.error("The Configure Generate run failed.checkAllExcelFile fail");
//			return;
//		}
//		List<String> generateXmlList = new ArrayList<>();
//		for (String server : com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.getSERVERS()) {
//			result = generateXmlFile(server, param, generateXmlList);
//			if (!result) {
//				logger.error("The Configure Generate run failed. server:{}", server);
//				return;
//			}
//		}
//		result = generateLanguageXmlFile();
//		if (!result) {
//			logger.error("The Configure Generate run failed. generateLanguageXmlFile");
//			return;
//		}
//		// 所有服务器生成没有问题 再拷贝xml
//		for (String server : com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.getSERVERS()) {
//			result = copyXmlFile(server);
//			if (!result) {
//				logger.error("The Configure Generate run failed.server:{}", server);
//				return;
//			}
//		}
//		for (String server : com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.getLanServers()) {
//			result = copyLanguageXmlFile(server);
//			if (!result) {
//				logger.error("The Configure Generate run failed. copyLanguageXmlFile");
//				return;
//			}
//		}
//		logger.error("success generate check copy ");
//	}
//
//	private static void clearTempFolder() {
//		String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//		File tmpDir = new File(tmpPath);
//		if (!tmpDir.exists()) {
//			return;
//		}
//		for (File xmlFile : Objects.requireNonNull(tmpDir.listFiles())) {
//			xmlFile.delete();
//		}
//		tmpDir.delete();
//	}
//
//	private static boolean copyLanguageXmlFile(String outDir) {
//		String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//		String outPath = System.getProperty("user.dir") + outDir;
//		for (String language : LANGUAGE_LIST) {
//			String fileName = "string_" + language;
//			boolean result = copySingleXmlFile(tmpPath, outPath, fileName);
//			if (!result) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static final String[] LANGUAGE_ARRAY = {
//			"en",
//			"zh_cn",
//			"ko",
//			"de",
//			"es",
//			"fr",
//			"it",
//			"ja",
//			"pt_br",
//			"ru",
//			"tr",
//			"zh_tw",
//			"zh_gn",
//			"vn",
//			"th",
//	};
//
//	private static final List<String> LANGUAGE_LIST = TableUtils.StringArrayToStringList(LANGUAGE_ARRAY);
//	private static final String FILED_ID = "ID";
//	private static final String FILED_TEXT = "text";
//
//	private static boolean generateLanguageXmlFile() {
//		try {
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			File xlsxFile = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.PA_PATH + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.LAN_DIR + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.LAN_PREFIX + ".xlsx");
//			if (!xlsxFile.exists()) {
//				logger.error("not find language xlsxFile");
//				return false;
//			}
//			InputStream inputStream = new FileInputStream(xlsxFile);
//			XSSFWorkbook xlsxBook = new XSSFWorkbook(inputStream);
//			Map<String, DOMElement> languageDataMap = new HashMap<>();
//			List<String> idList = new ArrayList<>();
//			for (String language : LANGUAGE_LIST) {
//				DOMElement root = new DOMElement("table");
//				DOMElement type = new DOMElement("type");
//				DOMElement item = new DOMElement("item");
//				item.addAttribute(FILED_ID, "string");
//				item.addAttribute(FILED_TEXT, "string");
//				type.add(item);
//				DOMElement data = new DOMElement("data");
//				root.add(type);
//				root.add(data);
//				languageDataMap.put(language, data);
//			}
//			for (int i = 0; i < xlsxBook.getNumberOfSheets(); i++) {
//				XSSFSheet xlsxSheet = xlsxBook.getSheetAt(i);
//				logger.error("generate language sheet:{}", xlsxSheet.getSheetName());
//				boolean result = generateLanguageContentExcelSheet(xlsxSheet, idList);
//				if (!result) {
//					xlsxBook.close();
//					return false;
//				}
//			}
//			for (String language : LANGUAGE_LIST) {
//				DOMElement data = languageDataMap.get(language);
//				DOMElement root = (DOMElement) data.getParent();
//				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
//				String content = formatXml(root, 0);
//				File outFile = new File(tmpPath + "string_" + language + ".xml");
//				if (!outFile.exists()) {
//					logger.error("generateLanguageXmlFile {} not exist create:{}", outFile.getName(), outFile.getParentFile().mkdirs());
//				}
//				FileOutputStream outputStream = new FileOutputStream(outFile, false);
//				String outputText = header + content;
//				outputStream.write(outputText.getBytes(StandardCharsets.UTF_8));
//				outputStream.close();
//			}
//			xlsxBook.close();
//			return true;
//		} catch (Exception e) {
//			logger.error("generate Language error", e);
//			return false;
//		}
//	}
//
//	private static boolean generateLanguageContentExcelSheet(XSSFSheet xlsxSheet, List<String> idList) {
//		List<String> fieldList = new ArrayList<>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			if (rowIndex == FIELD_ROW_INDEX) {
//				Row rowData = xlsxSheet.getRow(rowIndex);
//				if (rowData == null) {
//					logger.error("发现空白行 language sheetName:{} row:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, xlsxSheet.getLastRowNum() + 1);
//					return false;
//				}
//				for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//					Cell cellData = rowData.getCell(cellIndex);
//					if (cellData == null) {
//						logger.error("发现空白行 language sheetName:{} row:{} cell:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, xlsxSheet.getLastRowNum() + 1);
//						return false;
//					}
//					String cellStringData = getFormatCellData(cellData);
//					fieldList.add(cellStringData.replace("$", ""));
//				}
//			} else if (rowIndex >= DATA_ROW_INDEX) {
//				Row rowData = xlsxSheet.getRow(rowIndex);
//				if (rowData == null) {
//					logger.error("发现空白行 language sheetName:{} row:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, xlsxSheet.getLastRowNum() + 1);
//					return false;
//				}
//				String ID = "";
//				for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//					Cell cellData = rowData.getCell(cellIndex);
//					if (cellIndex == ID_CELL_INDEX) {
//						if (cellData == null) {
//							logger.error("发现空白行 language sheetName:{} row:{} cell:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, xlsxSheet.getLastRowNum() + 1);
//							return false;
//						}
//						String cellStringData = getFormatCellData(cellData);
//						if (cellStringData.isEmpty()) {
//							logger.error("ID empty language sheetName:{} row:{} cell:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, xlsxSheet.getLastRowNum() + 1);
//							return false;
//						}
//						ID = cellStringData;
//						if (idList.contains(ID)) {
//							logger.error("语言表 主键 ID 值重复 sheetName:{} ID:{}", xlsxSheet.getSheetName(), ID);
//							cellIndex = rowData.getLastCellNum();
//						} else {
//							idList.add(ID);
//						}
//					} else {
//						String cellStringData = "";
//						if (cellData != null) {
//							cellStringData = getFormatCellData(cellData);
//						}
//						String field = fieldList.get(cellIndex);
//						String xmlCellData = generateXmlFormatCellData("string", cellStringData);
//						if (LANGUAGE_LIST.contains(field)) {
//							DOMElement item = new DOMElement("item");
//							item.addAttribute(FILED_ID, ID);
//							item.addAttribute(FILED_TEXT, xmlCellData);
//						}
//					}
//				}
//			}
//		}
//		return true;
//	}
//
//	@SuppressWarnings("unchecked")
//	private static boolean copyXmlFile(String server) {
//		try {
//			String outPath = com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.AB_PATH + File.separator + server + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.resourcesXml;
//			clearFolder(outPath);
//
//			String resourcePath = com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.AB_PATH + File.separator + server + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.resources;
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			File resourceFile = new File(resourcePath);
//
//			if (!resourceFile.exists()) {
//				logger.error("copyXmlFile not find resourceFile:{}", outPath);
//				return false;
//			}
//			SAXReader saxReader = new SAXReader();
//			Document document = saxReader.read(resourceFile);
//			Element root = document.getRootElement();
//			List<Element> fileList = root.elements("file");
//			for (Element element : fileList) {
//				String fileName = element.attributeValue("name");
//				if (fileName.equals("Language")) {
//					continue;
//				}
//				boolean result = copySingleXmlFile(tmpPath, outPath, fileName);
//				if (!result) {
//					return false;
//				}
//			}
//			return true;
//		} catch (Exception e) {
//			logger.error("copy error", e);
//			return false;
//		}
//	}
//
//	private static final String TMP_XML_DIR = "\\_t__e___m____p_____\\";
//
//	@SuppressWarnings("unchecked")
//	private static boolean generateXmlFile(String resourceFilePath, String param, List<String> completeList) {
//		try {
//			String resourcePath = com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.AB_PATH + File.separator + resourceFilePath + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.resources;
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			File resourceFile = new File(resourcePath);
//			if (!resourceFile.exists()) {
//				logger.error("generateXmlFile not find resourceFile:{}", resourcePath);
//				return false;
//			}
//			SAXReader saxReader = new SAXReader();
//			Document document = saxReader.read(resourceFile);
//			Element root = document.getRootElement();
//			List<Element> fileList = root.elements("file");
//			for (Element element : fileList) {
//				String fileName = element.attributeValue("name");
//				if (completeList.contains(fileName)) {
//					continue;
//				}
//				if (fileName.equals("Language")) {
//					continue;
//				}
//				boolean result = generateSingleXmlFile(tmpPath, fileName, param);
//				if (!result) {
//					return false;
//				}
//				completeList.add(fileName);
//			}
//			return true;
//		} catch (Exception e) {
//			logger.error("generate error", e);
//			return false;
//		}
//	}
//
//	private static void clearFolder(String outPath) {
//		File outDir = new File(outPath);
//		if (!outDir.exists()) {
//			logger.error("clearFolder {} not exist create:{}", outDir.getName(), outDir.mkdir());
//		} else {
//			for (File xmlFile : Objects.requireNonNull(outDir.listFiles())) {
//				if (xmlFile.getName().endsWith(".xml")) {
//					logger.error("{} delete:{}", xmlFile.getName(), xmlFile.delete());
//				}
//			}
//		}
//	}
//
//	private static boolean copySingleXmlFile(String tmpDir, String outDir, String fileName) {
//		try {
//			Path srcFile = Paths.get(tmpDir + fileName + ".xml");
//			Path destFile = Paths.get(outDir + fileName + ".xml");
//			Files.copy(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
//			return true;
//		} catch (IOException e) {
//			logger.error("copy error", e);
//			return false;
//		}
//	}
//
//	private static File getXlsxFile(String fileName, String param) {
//		File xlsxFile = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".xlsx");
//		if (xlsxFile.exists()) {
//			return xlsxFile;
//		}
//		File xlsxFileServer = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".s.xlsx");
//		if (xlsxFileServer.exists()) {
//			return xlsxFileServer;
//		}
//		if (param.equals("-cn")) {
//			File xlsxFileCN = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".CN.xlsx");
//			if (xlsxFileCN.exists()) {
//				return xlsxFileCN;
//			}
//			File xlsxFileCNServer = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".CN.s.xlsx");
//			if (xlsxFileCNServer.exists()) {
//				return xlsxFileCNServer;
//			}
//		} else if (param.equals("-in")) {
//			File xlsxFileIN = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".IN.xlsx");
//			if (xlsxFileIN.exists()) {
//				return xlsxFileIN;
//			}
//			File xlsxFileINServer = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML + fileName + ".IN.s.xlsx");
//			if (xlsxFileINServer.exists()) {
//				return xlsxFileINServer;
//			}
//		}
//		return null;
//	}
//
//	private static boolean generateSingleXmlFile(String outDir, String fileName, String param) {
//		logger.error("generate file:{}", fileName);
//		try {
//			File xlsxFile = getXlsxFile(fileName, param);
//			if (xlsxFile == null) {
//				logger.error("not find xlsxFile:{}", fileName);
//				return false;
//			}
//			InputStream inputStream = new FileInputStream(xlsxFile);
//			XSSFWorkbook xlsxBook = new XSSFWorkbook(inputStream);
//			if (fileName.contains("TableConst")) {
//				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
//				DOMElement root = new DOMElement("table");
//				DOMElement type = new DOMElement("type");
//				DOMElement data = new DOMElement("data");
//				root.add(type);
//				root.add(data);
//				for (int i = 0; i < xlsxBook.getNumberOfSheets(); i++) {
//					XSSFSheet xlsxSheet = xlsxBook.getSheetAt(i);
//					logger.error("generate file:{} sheet:{}", fileName, xlsxSheet.getSheetName());
//					generateTableConstContentExcelSheet(xlsxSheet, type, data);
//				}
//				String content = formatXml(root, 0);
//				File outFile = new File(outDir + fileName + ".xml");
//				if (!outFile.exists()) {
//					logger.error("generateSingleXmlFile const {} not exist create:{}", outFile.getName(), outFile.getParentFile().mkdirs());
//				}
//				FileOutputStream outputStream = new FileOutputStream(outFile, false);
//				String outputText = header + content;
//				outputStream.write(outputText.getBytes(StandardCharsets.UTF_8));
//				outputStream.close();
//			} else {
//				XSSFSheet xlsxSheet = xlsxBook.getSheetAt(0);
//				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
//				DOMElement root = new DOMElement("table");
//				DOMElement type = new DOMElement("type");
//				DOMElement data = new DOMElement("data");
//				root.add(type);
//				root.add(data);
//				generateContentExcelSheet(xlsxSheet, type, data);
//				String content = formatXml(root, 0);
//				File outFile = new File(outDir + fileName + ".xml");
//				if (!outFile.exists()) {
//					logger.error("generateSingleXmlFile {} not exist create:{}", outFile.getName(), outFile.getParentFile().mkdirs());
//				}
//				FileOutputStream outputStream = new FileOutputStream(outFile, false);
//				String outputText = header + content;
//				outputStream.write(outputText.getBytes(StandardCharsets.UTF_8));
//				outputStream.close();
//			}
//			xlsxBook.close();
//			return true;
//		} catch (Exception e) {
//			logger.error("generate error", e);
//			return false;
//		}
//	}
//
//	private static void generateTableConstContentExcelSheet(XSSFSheet xlsxSheet, DOMElement type, DOMElement data) {
//		List<String> fieldList = new ArrayList<>();
//		List<String> typeList = new ArrayList<>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			if (rowIndex == DESC_ROW_INDEX) {
//				continue;
//			}
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			DOMElement item = new DOMElement("item");
//			String cellType = "";
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				String cellStringData = getFormatCellData(cellData);
//				if (cellIndex == TYPE_CELL_INDEX) {
//					cellType = cellStringData;
//				}
//				if (rowIndex == FIELD_ROW_INDEX) {
//					fieldList.add(cellStringData.replace("$", ""));
//				} else if (rowIndex == TYPE_ROW_INDEX) {
//					item.addAttribute(fieldList.get(cellIndex), cellStringData);
//					typeList.add(cellStringData);
//				} else if (rowIndex >= DATA_ROW_INDEX) {
//					String xmlCellData;
//					if (cellIndex == VALUE_CELL_INDEX) {
//						xmlCellData = generateXmlFormatCellData(cellType, cellStringData);
//					} else {
//						xmlCellData = generateXmlFormatCellData(typeList.get(cellIndex), cellStringData);
//					}
//					item.addAttribute(fieldList.get(cellIndex), xmlCellData);
//				}
//			}
//			if (rowIndex == TYPE_ROW_INDEX && !type.hasChildNodes()) {
//				type.add(item);
//			} else if (rowIndex >= DATA_ROW_INDEX) {
//				data.add(item);
//			}
//		}
//	}
//
//	private static void generateContentExcelSheet(XSSFSheet xlsxSheet, DOMElement type, DOMElement data) {
//		List<String> fieldList = new ArrayList<>();
//		List<String> typeList = new ArrayList<>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			if (rowIndex == DESC_ROW_INDEX) {
//				continue;
//			}
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			DOMElement item = new DOMElement("item");
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				String cellStringData = getFormatCellData(cellData);
//				if (rowIndex == FIELD_ROW_INDEX) {
//					fieldList.add(cellStringData.replace("$", ""));
//				} else if (rowIndex == TYPE_ROW_INDEX) {
//					item.addAttribute(fieldList.get(cellIndex), cellStringData);
//					typeList.add(cellStringData);
//				} else if (rowIndex >= DATA_ROW_INDEX) {
//					String cellType = typeList.get(cellIndex);
//					cellStringData = generateXmlFormatCellData(cellType, cellStringData);
//					item.addAttribute(fieldList.get(cellIndex), cellStringData);
//				}
//			}
//			if (rowIndex == TYPE_ROW_INDEX && !type.hasChildNodes()) {
//				type.add(item);
//			} else if (rowIndex >= DATA_ROW_INDEX) {
//				data.add(item);
//			}
//		}
//	}
//
//	private static String generateXmlFormatCellData(String cellType, String cellStringData) {
//		switch (cellType) {
//			case "int": {
//				double ret = Double.parseDouble(cellStringData);
//				int num = (int) ret;
//				return num + "";
//			}
//			case "long": {
//				double ret = Double.parseDouble(cellStringData);
//				long num = (int) ret;
//				return num + "";
//			}
//			default:
//				return cellStringData.replace("&", "&amp;").replace("<", "&lt;").replace("<", "&gt;").replace("\"", "&quot;").replace("\'", "&apos;");
//		}
//	}
//
//	private static final String[] INDENT_DEPTH = {
//			"",
//			"\t",
//			"\t\t",
//			"\t\t\t",
//			"\t\t\t\t",
//			"\t\t\t\t\t",
//			"\t\t\t\t\t\t",
//			"\t\t\t\t\t\t\t",
//			"\t\t\t\t\t\t\t\t",
//			"\t\t\t\t\t\t\t\t\t",
//			"\t\t\t\t\t\t\t\t\t\t" };
//
//	@SuppressWarnings("unchecked")
//	private static String formatXml(DOMElement root, int depth) {
//		String indent = INDENT_DEPTH[depth];
//		StringBuilder sb = new StringBuilder();
//		sb.append(indent).append("<").append(root.getTagName());
//		if (root.getAttributes().getLength() > 0) {
//			List<DefaultAttribute> attributes = root.attributes();
//			for (DefaultAttribute attribute : attributes) {
//				sb.append(" ").append(attribute.getName()).append("=\"").append(attribute.getValue()).append("\"");
//			}
//		}
//		if (root.getNodeValue() == null && root.getChildNodes() == null) {
//			sb.append("/>\n");
//		} else {
//			sb.append(">\n");
//			List<DOMElement> childs = root.elements();
//			for (DOMElement child : childs) {
//				sb.append(formatXml(child, depth + 1));
//			}
//			sb.append(indent).append("</").append(root.getTagName()).append(">\n");
//		}
//		return sb.toString();
//	}
//
//	private static boolean checkAllExcelFile(String param) {
//		File dir = new File(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML);
//		for (File excelFile : Objects.requireNonNull(dir.listFiles())) {
//			String fileName = excelFile.getName();
//			if (!fileName.endsWith(".xlsx")) {
//				continue;
//			}
//			if (param.equals("-in") && fileName.contains(".CN.")) {
//				continue;
//			} else if (param.equals("-cn") && fileName.contains(".IN.")) {
//				continue;
//			}
//			boolean result = checkSingleExcelFile(excelFile);
//			if (!result) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static boolean checkSingleExcelFile(File excelFile) {
//		logger.error("check file:{}", excelFile.getName());
//		try {
//			InputStream inputStream = new FileInputStream(excelFile);
//			XSSFWorkbook xlsxBook = new XSSFWorkbook(inputStream);
//			int sheetNumber = xlsxBook.getNumberOfSheets();
//			String fileName = excelFile.getName();
//			boolean result = false;
//			if (fileName.contains("TableConst")) {
//				List<String> idList = new ArrayList<>();
//				for (int i = 0; i < sheetNumber; i++) {
//					XSSFSheet xlsxSheet = xlsxBook.getSheetAt(i);
//					logger.error("check file:{} sheet:{}", excelFile.getName(), xlsxSheet.getSheetName());
//					result = checkSingleTableConstSheet(fileName, xlsxSheet, idList);
//					if (!result) {
//						xlsxBook.close();
//						return false;
//					}
//				}
//			} else {
//				XSSFSheet xlsxSheet = xlsxBook.getSheetAt(0);
//				result = checkSingleExcelSheet(fileName, xlsxSheet);
//			}
//			xlsxBook.close();
//			return result;
//		} catch (Exception e) {
//			logger.error("handle", e);
//			return false;
//		}
//	}
//
//	private static final int ID_CELL_INDEX = 0;
//	private static final int FILED_CELL_INDEX = 1;
//	private static final int TYPE_CELL_INDEX = 2;
//	private static final int VALUE_CELL_INDEX = 3;
//
//	private static final int FIELD_ROW_INDEX = 0;
//	private static final int TYPE_ROW_INDEX = 1;
//	private static final int DESC_ROW_INDEX = 2;
//	private static final int DATA_ROW_INDEX = 3;
//
//	private static boolean checkSingleTableConstSheet(String fileName, XSSFSheet xlsxSheet, List<String> idList) {
//		List<String> fieldList = new ArrayList<>();
//		List<String> typeList = new ArrayList<>();
//		int rowCount = xlsxSheet.getLastRowNum();
//		List<String> tableConstFieldList = new ArrayList<>();
//		for (int rowIndex = 0; rowIndex <= rowCount; rowIndex++) {
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			String tableConstFiled = "";
//			String tableConstFiledType = "";
//			String tableConstFiledValue = "";
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				String cellStringdata = getFormatCellData(cellData);
//				if (rowIndex <= TYPE_ROW_INDEX) {
//					if (cellStringdata.isEmpty()) {
//						logger.error("单元格数据不能为空 file:{} sheetName:{} row:{} cell:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1);
//						return false;
//					}
//					if (rowIndex == FIELD_ROW_INDEX) {
//						if (fieldList.contains(cellStringdata)) {
//							logger.error("表头 field 重复 file:{} sheetName:{} row:{} cell:{} field:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, cellStringdata);
//							return false;
//						}
//						fieldList.add(cellStringdata);
//					} else if (rowIndex == TYPE_ROW_INDEX) {
//						typeList.add(cellStringdata);
//					}
//				} else if (rowIndex >= DATA_ROW_INDEX) {
//					String fieldType = typeList.get(cellIndex);
//					if (!checkDataType(cellStringdata, fieldType)) {
//						logger.error("单元格 数据类型不匹配 file:{} sheetName:{} row:{} cell:{} type:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, typeList.get(cellIndex));
//						return false;
//					}
//					if (cellIndex == ID_CELL_INDEX) {
//						if (idList.contains(cellStringdata)) {
//							logger.error("主键 ID 值重复 file:{} sheetName:{} ID:{}", fileName, xlsxSheet.getSheetName(), cellStringdata);
//							return false;
//						}
//						idList.add(cellStringdata);
//					} else if (cellIndex == FILED_CELL_INDEX) {
//						tableConstFiled = cellStringdata;
//						if (tableConstFieldList.contains(tableConstFiled)) {
//							logger.error("TableConst fileName 重复 file:{} sheetName:{} fileName:{}", fileName, xlsxSheet.getSheetName(), tableConstFiled);
//							return false;
//						}
//					} else if (cellIndex == TYPE_CELL_INDEX) {
//						tableConstFiledType = cellStringdata;
//					} else if (cellIndex == VALUE_CELL_INDEX) {
//						tableConstFiledValue = cellStringdata;
//					}
//				}
//			}
//			if (rowIndex == TYPE_ROW_INDEX) {
//				if (fieldList.size() != typeList.size()) {
//					logger.error("表头配置错误 file:{} sheet:{}", fileName, xlsxSheet.getSheetName());
//					return false;
//				}
//			} else if (rowIndex >= DATA_ROW_INDEX) {
//				if (!checkDataType(tableConstFiledValue, tableConstFiledType)) {
//					logger.error("TableConst 数据类型不匹配 file:{} sheetName:{} row:{} type:{} value:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, tableConstFiledType, tableConstFiledValue);
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	private static boolean checkSingleExcelSheet(String fileName, XSSFSheet xlsxSheet) {
//		List<String> fieldList = new ArrayList<>();
//		List<String> typeList = new ArrayList<>();
//		List<String> idList = new ArrayList<>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			if (rowData == null) {
//				logger.error("发现空白行 file:{} sheetName:{} row:{} lastRow:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, xlsxSheet.getLastRowNum() + 1);
//				return false;
//			}
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				if (cellData == null) {
//					logger.error("发现空白行 file:{} sheetName:{} row:{} cell:{} lastRow:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, xlsxSheet.getLastRowNum() + 1);
//					return false;
//				}
//				String cellStringdata = getFormatCellData(cellData);
//				if (rowIndex <= TYPE_ROW_INDEX) {
//					if (cellStringdata.isEmpty()) {
//						logger.error("单元格数据不能为空 file:{} sheetName:{} row:{} cell:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1);
//						return false;
//					}
//					if (rowIndex == FIELD_ROW_INDEX) {
//						if (fieldList.contains(cellStringdata)) {
//							logger.error("表头 field 重复 file:{} sheetName:{} row:{} cell:{} field:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, cellStringdata);
//							return false;
//						}
//						fieldList.add(cellStringdata);
//					} else if (rowIndex == TYPE_ROW_INDEX) {
//						typeList.add(cellStringdata);
//					}
//				} else if (rowIndex >= DATA_ROW_INDEX) {
//					String fieldName = fieldList.get(cellIndex);
//					if (fieldName.startsWith("$$")) {
//						continue;
//					}
//					String fieldType = typeList.get(cellIndex);
//					if (!checkDataType(cellStringdata, fieldType)) {
//						logger.error("单元格 数据类型不匹配 file:{} sheetName:{} row:{} cell:{} type:{}", fileName, xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, typeList.get(cellIndex));
//						return false;
//					}
//					if (cellIndex == ID_CELL_INDEX) {
//						if (idList.contains(cellStringdata)) {
//							logger.error("主键 ID 值重复 file:{} sheetName:{} ID:{}", fileName, xlsxSheet.getSheetName(), cellStringdata);
//							return false;
//						}
//						idList.add(cellStringdata);
//					}
//				}
//			}
//			if (rowIndex == TYPE_ROW_INDEX) {
//				if (fieldList.size() != typeList.size()) {
//					logger.error("表头配置错误 file:{} sheet:{}", fileName, xlsxSheet.getSheetName());
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	private static boolean checkDataType(String data, String type) {
//		if (type.endsWith("[]")) {
//			return checkArray(data, type.substring(0, type.length() - 2));
//		} else {
//			switch (type) {
//				case "float":
//					return checkFloat(data);
//				case "int":
//					return checkInt(data);
//				case "long":
//					return checkLong(data);
//				case "string":
//					return true;
//				case "bool":
//					return checkBool(data);
//				case "Vector2":
//					return checkVector2(data);
//				case "Vector3":
//					return checkVector3(data);
//				case "Timestamp":
//					return checkTimestamp(data);
//				default:
//					return false;
//			}
//		}
//	}
//
//	private static boolean checkTimestamp(String data) {
//		try {
//			Timestamp.valueOf(data);
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private static boolean checkVector3(String data) {
//		data = data.replace("(", "");
//		data = data.replace(")", "");
//		String[] farray = data.split(",");
//		return farray.length == 3 && checkFloat(farray[0]) && checkFloat(farray[1]) && checkFloat(farray[2]);
//	}
//
//	private static boolean checkVector2(String data) {
//		data = data.replace("(", "");
//		data = data.replace(")", "");
//		String[] farray = data.split(",");
//		return farray.length == 2 && checkFloat(farray[0]) && checkFloat(farray[1]);
//	}
//
//	private static boolean checkBool(String data) {
//		if (data.equals("1") || data.equals("0")) {
//			return true;
//		}
//		if (data.equals("true") || data.equals("false")) {
//			return true;
//		}
//		try {
//			return Boolean.parseBoolean(data);
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private static boolean checkLong(String data) {
//		try {
//			Double ret = Double.parseDouble(data);
//			ret.longValue();
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private static boolean checkInt(String data) {
//		try {
//			Double ret = Double.parseDouble(data);
//			ret.intValue();
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private static boolean checkFloat(String data) {
//		try {
//			Double ret = Double.parseDouble(data);
//			ret.floatValue();
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	private static boolean checkArray(String data, String type) {
//		String[] arrays = parseArray(data);
//		if (arrays.length <= 0) {
//			return true;
//		}
//		for (String tmpData : arrays) {
//			if (!checkDataType(tmpData, type)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static String[] parseArray(String data) {
//		boolean isClass = data.contains("(");
//		data = data.trim();
//		data = data.replace("[", "");
//		data = data.replace("]", "");
//		data = data.replace("),", ")|");
//		data = data.replace("(", "");
//		data = data.replace(")", "");
//		if (data.equals("")) {
//			return new String[0];
//		}
//		String[] arrays;
//		if (isClass) {
//			arrays = data.split("\\|");
//		} else {
//			arrays = data.split(",");
//		}
//		return arrays;
//	}
//
//	private static String getCellData(Cell cellData) {
//		String cellText;
//		if (cellData.getCellType() == CellType.STRING) {
//			cellText = cellData.getStringCellValue();
//		} else if (cellData.getCellType() == CellType.NUMERIC) {
//			cellText = cellData.getNumericCellValue() + "";
//		} else if (cellData.getCellType() == CellType.BOOLEAN) {
//			cellText = cellData.getBooleanCellValue() + "";
//		} else if (cellData.getCellType() == CellType.BLANK) {
//			cellText = "";
//		} else if (cellData.getCellType() == CellType.FORMULA) {
//			if (cellData.getCachedFormulaResultType() == CellType.NUMERIC) {
//				cellText = cellData.getNumericCellValue() + "";
//			} else if (cellData.getCachedFormulaResultType() == CellType.STRING) {
//				cellText = cellData.getStringCellValue();
//			} else if (cellData.getCachedFormulaResultType() == CellType.BOOLEAN) {
//				cellText = cellData.getBooleanCellValue() + "";
//			} else if (cellData.getCellType() == CellType.ERROR) {
//				cellText = "";
//			} else {
//				logger.error("unhandle cellType:{} cachedFormulaResultType:{}", cellData.getCellType(), cellData.getCachedFormulaResultType());
//				return null;
//			}
//		} else if (cellData.getCellType() == CellType.ERROR) {
//			cellText = "";
//		} else {
//			logger.error("unhandle cellType:{}", cellData.getCellType());
//			return null;
//		}
//		return cellText;
//	}
//
//	public static String getFormatCellData(Cell cellData) {
//		return getCellData(cellData);
//	}
//
//}
