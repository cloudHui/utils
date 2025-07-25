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
//public class ExcelToXmlUtil {
//
//	private static final ICFLogger logger = CFLoggerFactory.getLogger(ExcelToXmlUtil.class);
//
//	private static final String EXCEL_DIR = "..\\..\\..\\Document\\Configure\\kongzhongtest\\XML\\";
//	private static final String LANGUAGE_FILE = "..\\..\\..\\Languages\\languages.xlsx";
//
//	private static final String BATTLE_MANAGER_RESOURCE = "..\\..\\..\\ServerJava\\if2-battleManagerServer\\resources\\resource.xml";
//	private static final String BATTLE_MANAGER_XML_DIR = "..\\..\\..\\ServerJava\\if2-battleManagerServer\\resources\\xml\\";
//
//	private static final String BATTLE_RESOURCE = "..\\..\\..\\ServerJava\\if2-battleServer\\resources\\resource.xml";
//	private static final String BATTLE_XML_DIR = "..\\..\\..\\ServerJava\\if2-battleServer\\resources\\xml\\";
//
//	private static final String GAME_RESOURCE = "..\\..\\..\\ServerJava\\if2-gameServer\\resources\\resource.xml";
//	private static final String GAME_XML_DIR = "..\\..\\..\\ServerJava\\if2-gameServer\\resources\\xml\\";
//
//	private static final String GATEWAY_RESOURCE = "..\\..\\..\\ServerJava\\if2-gatewayServer\\resources\\resource.xml";
//	private static final String GATEWAY_XML_DIR = "..\\..\\..\\ServerJava\\if2-gatewayServer\\resources\\xml\\";
//
//	private static final String GMORDER_RESOURCE = "..\\..\\..\\ServerJava\\if2-gmOrderServer\\resources\\resource.xml";
//	private static final String GMORDER_XML_DIR = "..\\..\\..\\ServerJava\\if2-gmOrderServer\\resources\\xml\\";
//
//	private static final String HTTP_RESOURCE = "..\\..\\..\\ServerJava\\if2-httpServer\\resources\\resource.xml";
//	private static final String HTTP_XML_DIR = "..\\..\\..\\ServerJava\\if2-httpServer\\resources\\xml\\";
//
//	private static final String NOTIFIC_RESOURCE = "..\\..\\..\\ServerJava\\if2-notificServer\\resources\\resource.xml";
//	private static final String NOTIFIC_XML_DIR = "..\\..\\..\\ServerJava\\if2-notificServer\\resources\\xml\\";
//
//	private static final String PT_RESOURCE = "..\\..\\..\\ServerJava\\if2-pressureTestServer\\resources\\resource.xml";
//	private static final String PT_XML_DIR = "..\\..\\..\\ServerJava\\if2-pressureTestServer\\resources\\xml\\";
//
//	private static final String ROUTER_RESOURCE = "..\\..\\..\\ServerJava\\if2-routerServer\\resources\\resource.xml";
//	private static final String ROUTER_XML_DIR = "..\\..\\..\\ServerJava\\if2-routerServer\\resources\\xml\\";
//
//	private static final String SOCIAL_RESOURCE = "..\\..\\..\\ServerJava\\if2-socialServer\\resources\\resource.xml";
//	private static final String SOCIAL_XML_DIR = "..\\..\\..\\ServerJava\\if2-socialServer\\resources\\xml\\";
//
//	public static void main(String[] args) {
//		CFLoggerFactory.initDefault();
//		String param = "-in";
//		if (args != null && args.length > 0) {
//			if (args[0].equals("-cn")) {
//				param = args[0];
//			} else if (args[0].equals("-in")) {
//				param = args[0];
//			}
//		}
//		boolean result = checkAllExcelFile(param);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		// 所有服务器 先生成xml
//		List<String> generateXmlList = new ArrayList<>();
//		result = generateXmlFile(BATTLE_MANAGER_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(BATTLE_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(GAME_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(GATEWAY_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(GMORDER_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(HTTP_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(NOTIFIC_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(PT_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(ROUTER_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateXmlFile(SOCIAL_RESOURCE, param, generateXmlList);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = generateLanguageXmlFile();
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		// 所有服务器生成没有问题 再拷贝xml
//		result = copyXmlFile(BATTLE_MANAGER_RESOURCE, BATTLE_MANAGER_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(BATTLE_RESOURCE, BATTLE_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(GAME_RESOURCE, GAME_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(GATEWAY_RESOURCE, GATEWAY_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(GMORDER_RESOURCE, GMORDER_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(GMORDER_RESOURCE, GMORDER_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(HTTP_RESOURCE, HTTP_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(NOTIFIC_RESOURCE, NOTIFIC_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(PT_RESOURCE, PT_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(PT_RESOURCE, PT_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(ROUTER_RESOURCE, ROUTER_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyXmlFile(SOCIAL_RESOURCE, SOCIAL_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyLanguageXmlFile(GAME_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyLanguageXmlFile(HTTP_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		result = copyLanguageXmlFile(NOTIFIC_XML_DIR);
//		if (!result) {
//			logger.error("The Configure Generate run failed.");
//			return;
//		}
//		clearTempFolder();
//		logger.error("The Configure Generate run success.");
//	}
//
//	private static void clearTempFolder() {
//		String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//		File tmpDir = new File(tmpPath);
//		if (!tmpDir.exists()) {
//			return;
//		}
//		for (File xmlFile : tmpDir.listFiles()) {
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
//			boolean result = copySingleXmlFile(tmpPath, outPath, fileName, fileName);
//			if (!result) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static String[] LANGUAGE_ARRAY = {
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
//	private static List<String> LANGUAGE_LIST = TableUtils.StringArrayToStringList(LANGUAGE_ARRAY);
//	private static String FILED_ID = "ID";
//	private static String FILED_TEXT = "text";
//
//	private static boolean generateLanguageXmlFile() {
//		try {
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			File xlsxFile = new File(System.getProperty("user.dir") + LANGUAGE_FILE);
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
//				boolean result = generateLanguageContentExcelSheet(xlsxSheet, languageDataMap, idList);
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
//					outFile.getParentFile().mkdirs();
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
//	private static boolean generateLanguageContentExcelSheet(XSSFSheet xlsxSheet, Map<String, DOMElement> languageDataMap, List<String> idList) {
//		List<String> fieldList = new ArrayList<String>();
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
//					String cellStringData = getCellData(cellData);
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
//						String cellStringData = getCellData(cellData);
//						if (cellStringData.isEmpty()) {
//							logger.error("ID empty language sheetName:{} row:{} cell:{} lastRow:{}", xlsxSheet.getSheetName(), rowIndex + 1, cellIndex + 1, xlsxSheet.getLastRowNum() + 1);
//							return false;
//						}
//						ID = cellStringData;
//						if (idList.contains(ID)) {
//							logger.error("语言表 主键 ID 值重复 sheetName:{} ID:{}", xlsxSheet.getSheetName(), ID);
//							cellIndex = rowData.getLastCellNum();
//							continue;
//						} else {
//							idList.add(ID);
//						}
//					} else {
//						String cellStringData = "";
//						if (cellData != null) {
//							cellStringData = getCellData(cellData);
//						}
//						String field = fieldList.get(cellIndex);
//						String xmlCellData = generateXmlFormatCellData("string", cellStringData);
//						if (LANGUAGE_LIST.contains(field) && !ID.isEmpty()) {
//							DOMElement item = new DOMElement("item");
//							item.addAttribute(FILED_ID, ID);
//							item.addAttribute(FILED_TEXT, xmlCellData);
//							DOMElement data = languageDataMap.get(field);
//							data.add(item);
//						}
//					}
//				}
//			}
//		}
//		return true;
//	}
//
//	@SuppressWarnings("unchecked")
//	private static boolean copyXmlFile(String resourceFilePath, String outDir) {
//		try {
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			String outPath = System.getProperty("user.dir") + outDir;
//			clearFolder(outPath);
//			File resourceFile = new File(System.getProperty("user.dir") + resourceFilePath);
//			if (resourceFile == null || !resourceFile.exists()) {
//				logger.error("not find resourceFile:{}", resourceFilePath);
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
//				boolean result = copySingleXmlFile(tmpPath, outPath, fileName, fileName);
//				if (!result) {
//					return false;
//				}
//				String nextResource = element.attributeValue("nextResource");
//				if (nextResource == null || nextResource.isEmpty()) {
//					continue;
//				}
//				result = copySingleXmlFile(tmpPath, outPath, fileName, fileName + "Next");
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
//			String tmpPath = System.getProperty("user.dir") + TMP_XML_DIR;
//			File resourceFile = new File(System.getProperty("user.dir") + resourceFilePath);
//			if (resourceFile == null || !resourceFile.exists()) {
//				logger.error("not find resourceFile:{}", resourceFilePath);
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
//			outDir.mkdir();
//		} else {
//			for (File xmlFile : outDir.listFiles()) {
//				if (xmlFile.getName().endsWith(".xml")) {
//					xmlFile.delete();
//				}
//			}
//		}
//	}
//
//	private static boolean copySingleXmlFile(String tmpDir, String outDir, String fileName, String outFileName) {
//		try {
//			Path srcFile = Paths.get(tmpDir + fileName + ".xml");
//			Path destFile = Paths.get(outDir + outFileName + ".xml");
//			Files.copy(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
//			return true;
//		} catch (IOException e) {
//			logger.error("copy error", e);
//			return false;
//		}
//	}
//
//	private static File getXlsxFile(String fileName, String param) {
//		File xlsxFile = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".xlsx");
//		if (xlsxFile.exists()) {
//			return xlsxFile;
//		}
//		File xlsxFileServer = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".s.xlsx");
//		if (xlsxFileServer.exists()) {
//			return xlsxFileServer;
//		}
//		if (param.equals("-cn")) {
//			File xlsxFileCN = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".CN.xlsx");
//			if (xlsxFileCN.exists()) {
//				return xlsxFileCN;
//			}
//			File xlsxFileCNServer = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".CN.s.xlsx");
//			if (xlsxFileCNServer.exists()) {
//				return xlsxFileCNServer;
//			}
//		} else if (param.equals("-in")) {
//			File xlsxFileIN = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".IN.xlsx");
//			if (xlsxFileIN.exists()) {
//				return xlsxFileIN;
//			}
//			File xlsxFileINServer = new File(System.getProperty("user.dir") + EXCEL_DIR + fileName + ".IN.s.xlsx");
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
//					generateTableConstContentExcelSheet(fileName, xlsxSheet, type, data);
//				}
//				String content = formatXml(root, 0);
//				File outFile = new File(outDir + fileName + ".xml");
//				if (!outFile.exists()) {
//					outFile.getParentFile().mkdirs();
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
//				generateContentExcelSheet(fileName, xlsxSheet, type, data);
//				String content = formatXml(root, 0);
//				File outFile = new File(outDir + fileName + ".xml");
//				if (!outFile.exists()) {
//					outFile.getParentFile().mkdirs();
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
//	private static void generateTableConstContentExcelSheet(String fileName, XSSFSheet xlsxSheet, DOMElement type, DOMElement data) {
//		List<String> fieldList = new ArrayList<String>();
//		List<String> typeList = new ArrayList<String>();
//		List<String> ignoreList = new ArrayList<String>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			if (rowIndex == DESC_ROW_INDEX) {
//				continue;
//			}
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			DOMElement item = new DOMElement("item");
//			String cellType = "";
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				String cellStringData = getCellData(cellData);
//				if (cellIndex == TYPE_CELL_INDEX) {
//					cellType = cellStringData;
//				}
//				if (rowIndex == FIELD_ROW_INDEX) {
//					if (cellStringData.startsWith("$$")) {
//						ignoreList.add(cellStringData.replace("$", ""));
//					}
//					fieldList.add(cellStringData.replace("$", ""));
//				} else if (rowIndex == TYPE_ROW_INDEX) {
//					String filedName = fieldList.get(cellIndex);
//					if (!ignoreList.contains(filedName)) {
//						item.addAttribute(fieldList.get(cellIndex), cellStringData);
//					}
//					typeList.add(cellStringData);
//				} else if (rowIndex >= DATA_ROW_INDEX) {
//					String filedName = fieldList.get(cellIndex);
//					if (!ignoreList.contains(filedName)) {
//						String xmlCellData;
//						if (cellIndex == VALUE_CELL_INDEX) {
//							xmlCellData = generateXmlFormatCellData(cellType, cellStringData);
//						} else {
//							xmlCellData = generateXmlFormatCellData(typeList.get(cellIndex), cellStringData);
//						}
//						item.addAttribute(fieldList.get(cellIndex), xmlCellData);
//					}
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
//	private static void generateContentExcelSheet(String fileName, XSSFSheet xlsxSheet, DOMElement type, DOMElement data) {
//		List<String> fieldList = new ArrayList<String>();
//		List<String> typeList = new ArrayList<String>();
//		for (int rowIndex = 0; rowIndex <= xlsxSheet.getLastRowNum(); rowIndex++) {
//			if (rowIndex == DESC_ROW_INDEX) {
//				continue;
//			}
//			Row rowData = xlsxSheet.getRow(rowIndex);
//			DOMElement item = new DOMElement("item");
//			for (int cellIndex = 0; cellIndex < rowData.getLastCellNum(); cellIndex++) {
//				Cell cellData = rowData.getCell(cellIndex);
//				String cellStringData = getCellData(cellData);
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
//				Double ret = Double.parseDouble(cellStringData);
//				int num = ret.intValue();
//				return num + "";
//			}
//			case "long": {
//				Double ret = Double.parseDouble(cellStringData);
//				long num = ret.intValue();
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
//		File dir = new File(System.getProperty("user.dir") + EXCEL_DIR);
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
//				String cellStringdata = getCellData(cellData);
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
//				String cellStringdata = getCellData(cellData);
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
//	public static String getCellData(Cell cellData) {
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
//	public static String getFormatXmlCellData(Cell cellData) {
//		String cellText = getCellData(cellData);
//		if (cellText == null || cellText.isEmpty()) {
//			return cellText;
//		}
//		cellText = cellText.replace("\r\n", "");
//		return cellText;
//	}
//}
