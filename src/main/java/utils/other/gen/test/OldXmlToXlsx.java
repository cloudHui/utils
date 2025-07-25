//package utils.other.gen.test;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import com.coolfish.framework.commons.log.CFLoggerFactory;
//import com.coolfish.framework.commons.log.ICFLogger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
///**
// * @author admin
// * @className XmlToExcel
// * @description
// * @createDate 2025/7/22 10:10
// */
//public class OldXmlToXlsx {
//	private static final ICFLogger logger = CFLoggerFactory.getLogger(OldXmlToXlsx.class);
//
//	static {
//		CFLoggerFactory.initDefault();
//	}
//
//	/**
//	 * 把原始的旧xml 转成 xlsx
//	 */
//	public static void generateXmlToXlsx(String param) {
//		generate(param);
//		generateLan();
//	}
//
//	private static void generate(String param) {
//		Set<String> useTable = com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.getUseTable(param);
//		Map<String, File> unUseTable = new HashMap<>();
//		List<String> delete = new ArrayList<>();
//		for (File excelFile : com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.getAllXmlFiles(com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.ALL_XML, ".xml", param).values()) {
//			if (excelFile != null) {
//				String name = excelFile.getName();
//				name = name.split("\\.")[0];
//				if (useTable.contains(name)) {
//					if (generateXmlToXlsx(excelFile)) {
//						delete.add(excelFile.getName());
//					}
//				} else {
//					unUseTable.put(name, excelFile);
//				}
//			}
//		}
//		for (File excelFile : unUseTable.values()) {
//			logger.error("服务器未使用表 {}", excelFile.getName());
//		}
//		// deleteOld(delete, allXmlFiles);
//	}
//
//	private static void generateLan() {
//		String resourcePath = com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.PA_PATH + File.separator + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.LAN_DIR + com.coolfish.ironforce2.configure.utils.paraser.GeneratorJava.LAN_PREFIX + ".xml";
//		File file = new File(resourcePath);
//		if (!file.exists()) {
//			logger.error("{} 没有这个 文件 ", file.getName());
//		} else {
//			boolean success = generateXmlToXlsx(file);
//			logger.error("generateLan Xml to xlsx success:{}", success);
//			// Todo 打开删除
//			// if (success) {
//			// boolean delete = file.delete();
//			// logger.error("after generateLan Xml to xlsx delete:{}", delete);
//			// }
//		}
//	}
//
//	private static void deleteOld(List<String> delete, Map<String, File> allXmlFiles) {
//		List<String> deleteSuccess = new ArrayList<>();
//		List<String> deleteFail = new ArrayList<>();
//		for (String name : delete) {
//			File file = allXmlFiles.get(name);
//			if (file != null && file.exists()) {
//				if (file.delete()) {
//					deleteSuccess.add(name);
//					continue;
//				}
//			}
//			deleteFail.add(name);
//		}
//		logger.error("删除成功:{}", deleteSuccess.toString());
//		logger.error("删除失败:{}", deleteFail.toString());
//
//	}
//
//	// ================ 核心生成逻辑 ================
//	private static boolean generateXmlToXlsx(File xmlFile) {
//		try {
//			// 1. 解析XML文件
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(xmlFile);
//			doc.getDocumentElement().normalize();
//
//			// 2. 创建新的XLSX工作簿
//			Workbook xlsxWorkbook = new XSSFWorkbook();
//
//			// 3. 获取所有工作表节点
//			NodeList worksheetNodes = doc.getElementsByTagName("Worksheet");
//
//			// 4. 遍历每个工作表
//			for (int wsIndex = 0; wsIndex < worksheetNodes.getLength(); wsIndex++) {
//				Element worksheetElement = (Element) worksheetNodes.item(wsIndex);
//
//				// 5. 获取并验证工作表名称
//				String sheetName = getWorksheetName(worksheetElement, wsIndex);
//
//				// 6. 创建工作表并处理内容
//				processWorksheet(xlsxWorkbook, worksheetElement, sheetName);
//			}
//			String absolutePath = xmlFile.getAbsolutePath();
//			absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(".")) + ".xlsx";
//			// 17. 保存为XLSX文件
//			saveOutputFile(xlsxWorkbook, absolutePath);
//
//			logger.error("转换成功！输出文件: {}", absolutePath);
//			return true;
//		} catch (Exception e) {
//			logger.error("转换过程中发生错误 ", e);
//		}
//		return false;
//	}
//
//	private static String getWorksheetName(Element worksheetElement, int index) {
//		String sheetName = worksheetElement.getAttribute("ss:Name");
//		return sheetName.isEmpty() ? "Sheet" + (index + 1) : sheetName;
//	}
//
//	private static void processWorksheet(Workbook workbook, Element worksheetElement, String sheetName) {
//		// 7. 创建新工作表
//		Sheet sheet = workbook.createSheet(sheetName);
//
//		// 8. 获取表格数据
//		NodeList tableNodes = worksheetElement.getElementsByTagName("Table");
//		if (tableNodes.getLength() == 0)
//			return;
//
//		Element tableElement = (Element) tableNodes.item(0);
//		NodeList rows = tableElement.getElementsByTagName("Row");
//
//		// 9. 存储列宽信息
//		Map<Integer, Integer> columnWidths = new HashMap<>();
//		int rowNum = 0;
//
//		// 10. 处理每一行
//		for (int i = 0; i < rows.getLength(); i++) {
//			Element rowElement = (Element) rows.item(i);
//			Row row = sheet.createRow(rowNum++);
//			processRowCells(row, rowElement, columnWidths);
//		}
//
//		// 16. 应用列宽
//		applyColumnWidths(sheet, columnWidths);
//	}
//
//	private static void processRowCells(Row row, Element rowElement, Map<Integer, Integer> columnWidths) {
//		NodeList cells = rowElement.getElementsByTagName("Cell");
//
//		for (int j = 0; j < cells.getLength(); j++) {
//			Element cellElement = (Element) cells.item(j);
//			int cellIndex = getCellIndex(cellElement, j);
//
//			// 确保单元格位置正确
//			fillMissingCells(row, cellIndex);
//
//			Cell cell = row.createCell(cellIndex);
//			setCellValue(cell, cellElement);
//
//			// 更新列宽
//			updateColumnWidth(cell, cellIndex, columnWidths);
//		}
//	}
//
//	private static int getCellIndex(Element cellElement, int defaultIndex) {
//		if (cellElement.hasAttribute("ss:Index")) {
//			return Integer.parseInt(cellElement.getAttribute("ss:Index")) - 1;
//		}
//		return defaultIndex;
//	}
//
//	private static void fillMissingCells(Row row, int targetIndex) {
//		while (row.getLastCellNum() < targetIndex && row.getLastCellNum() > 0) {
//			try {
//				row.createCell(row.getLastCellNum());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private static void setCellValue(Cell cell, Element cellElement) {
//		NodeList dataNodes = cellElement.getElementsByTagName("Data");
//		if (dataNodes.getLength() == 0)
//			return;
//
//		Element dataElement = (Element) dataNodes.item(0);
//		String cellValue = dataElement.getTextContent();
//		String dataType = dataElement.getAttribute("ss:Type");
//
//		// 根据数据类型设置单元格值
//		if ("Number".equalsIgnoreCase(dataType)) {
//			try {
//				cell.setCellValue(Double.parseDouble(cellValue));
//			} catch (NumberFormatException e) {
//				cell.setCellValue(cellValue);
//			}
//		} else {
//			cell.setCellValue(cellValue);
//		}
//	}
//
//	private static void updateColumnWidth(Cell cell, int columnIndex, Map<Integer, Integer> columnWidths) {
//		String cellValue = com.coolfish.ironforce2.configure.utils.paraser.ExcelToXmlUtil.getCellData(cell);
//
//		int currentWidth = columnWidths.getOrDefault(columnIndex, 0);
//		int textWidth = cellValue.length() * 256; // 字符宽度估算
//
//		if (textWidth > currentWidth) {
//			columnWidths.put(columnIndex, textWidth);
//		}
//	}
//
//	private static void applyColumnWidths(Sheet sheet, Map<Integer, Integer> columnWidths) {
//		for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
//			int maxWidth = Math.min(entry.getValue(), 255 * 256); // 最大宽度限制
//			sheet.setColumnWidth(entry.getKey(), maxWidth);
//		}
//	}
//
//	private static void saveOutputFile(Workbook workbook, String fileName) throws Exception {
//		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
//			workbook.write(outputStream);
//			workbook.close();
//		}
//	}
//}
