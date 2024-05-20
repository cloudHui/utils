package utils.utils.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	/**
	 * 导出Excel
	 *
	 * @param sheetName sheet名称
	 * @param title     标题
	 * @param wb        HSSFWorkbook对象
	 */
	public static void setHSSFWorkbookTitle(String sheetName, String[] title, HSSFWorkbook wb) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
		if (wb == null) {
			wb = new HSSFWorkbook();
		}

		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

		//声明列对象
		HSSFCell cell;

		//创建标题
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
	}

	/**
	 * 添加内容
	 *
	 * @param sheet  一个sheet
	 * @param values 内容
	 */
	public static void addContent(HSSFSheet sheet, String[][] values) {
		HSSFRow rowContent;
		int last = sheet.getLastRowNum();
		//创建内容
		for (int i = last, begin = 0; i < last + values.length; i++, begin++) {
			rowContent = sheet.createRow(i + 1);
			for (int j = 0; j < values[begin].length; j++) {
				//将内容按顺序赋给对应的列对象
				rowContent.createCell(j).setCellValue(values[begin][j]);
			}
		}
	}

	/**
	 * 文件输出
	 *
	 * @param workbook 填充好的workbook
	 * @param path     存放的位置
	 * @author LiQuanhui
	 * @date 2017年11月24日 下午5:26:23
	 */
	public static void outFile(HSSFWorkbook workbook, String path) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			assert os != null;
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Excel文件的内容
	 *
	 * @return 以List返回excel中内容
	 */
	public static List<Map<String, String>> readExcel(String fileName) {
		//读取 xls格式的文件需要使用 HSSFWorkbook；
		//读取 xlsx 格式的文件需要使用 XSSFWorkbook；
		String last = fileName.substring(fileName.indexOf(".") + 1);
		List<Map<String, String>> list = new ArrayList<>();
		InputStream inputStream;
		if (last.equals("xlsx")) {
			try {
				inputStream = new FileInputStream(fileName);
				//定义工作簿
				XSSFWorkbook xssfWorkbook = null;
				try {
					xssfWorkbook = new XSSFWorkbook(inputStream);
				} catch (Exception e) {
					System.out.println("Excel data file cannot be found!");
				}
				if (xssfWorkbook != null) {
					//定义工作表
					XSSFSheet xssfSheet;
					xssfSheet = xssfWorkbook.getSheetAt(0);
					if (xssfSheet != null) {
						//定义行
						//默认第一行为标题行，index = 0
						XSSFRow titleRow = xssfSheet.getRow(0);
						//循环取每行的数据
						for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
							XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
							if (xssfRow != null) {
								Map<String, String> map = new LinkedHashMap<>();
								//循环取每个单元格(cell)的数据
								for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
									XSSFCell titleCell = titleRow.getCell(cellIndex);
									XSSFCell xssfCell = xssfRow.getCell(cellIndex);
									map.put(getString(titleCell), getString(xssfCell));
								}
								list.add(map);
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else if (last.equals("xls")) {
			try {
				inputStream = new FileInputStream(fileName);
				//定义工作簿
				HSSFWorkbook xssfWorkbook = null;
				try {
					xssfWorkbook = new HSSFWorkbook(inputStream);
				} catch (Exception e) {
					System.out.println("Excel data file cannot be found!");
				}
				if (xssfWorkbook != null) {
					//定义工作表
					HSSFSheet xssfSheet;
					xssfSheet = xssfWorkbook.getSheetAt(0);
					if (xssfSheet != null) {
						//定义行
						//默认第一行为标题行，index = 0
						HSSFRow titleRow = xssfSheet.getRow(0);
						//循环取每行的数据
						for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
							HSSFRow xssfRow = xssfSheet.getRow(rowIndex);
							if (xssfRow != null) {
								Map<String, String> map = new LinkedHashMap<>();
								//循环取每个单元格(cell)的数据
								for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
									HSSFCell titleCell = titleRow.getCell(cellIndex);
									HSSFCell xssfCell = xssfRow.getCell(cellIndex);
									map.put(getString(titleCell), getString(xssfCell));
								}
								list.add(map);
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 把单元格的内容转为字符串
	 *
	 * @param xssfCell 单元格
	 * @return 字符串
	 */
	private static String getString(XSSFCell xssfCell) {
		if (xssfCell == null) {
			return "";
		}
		if (xssfCell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(xssfCell.getNumericCellValue());
		} else if (xssfCell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else {
			return xssfCell.getStringCellValue();
		}
	}

	/**
	 * 把单元格的内容转为字符串
	 *
	 * @param xssfCell 单元格
	 * @return 字符串
	 */
	private static String getString(HSSFCell xssfCell) {
		if (xssfCell == null) {
			return "";
		}
		if (xssfCell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(xssfCell.getNumericCellValue());
		} else if (xssfCell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else {
			return xssfCell.getStringCellValue();
		}
	}

	/**
	 * 把内容写入Excel
	 *
	 * @param list         传入要写的内容，此处以一个List内容为例，先把要写的内容放到一个list中
	 * @param outputStream 把输出流怼到要写入的Excel上，准备往里面写数据
	 */
	public static void writeExcel(List<List> list, OutputStream outputStream) {
		//创建工作簿
		XSSFWorkbook xssfWorkbook;
		xssfWorkbook = new XSSFWorkbook();

		//创建工作表
		XSSFSheet xssfSheet;
		xssfSheet = xssfWorkbook.createSheet();

		//创建行
		XSSFRow xssfRow;

		//创建列，即单元格Cell
		XSSFCell xssfCell;

		//把List里面的数据写到excel中
		for (int i = 0; i < list.size(); i++) {
			//从第一行开始写入
			xssfRow = xssfSheet.createRow(i);
			//创建每个单元格Cell，即列的数据
			List sub_list = list.get(i);
			for (int j = 0; j < sub_list.size(); j++) {
				xssfCell = xssfRow.createCell(j); //创建单元格
				xssfCell.setCellValue((String) sub_list.get(j)); //设置单元格内容
			}
		}

		//用输出流写到excel
		try {
			xssfWorkbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Excel文件的内容 创建文件
	 *
	 * @return 以List返回excel中内容
	 */
	public static void readExcelCreateJavaHead(String fileName) {
		//读取 xls格式的文件需要使用 HSSFWorkbook;
		//读取 xlsx 格式的文件需要使用 XSSFWorkbook;
		String javaName = ExcelToJavaGenerator.capitalize(fileName.split("\\.")[0]);
		String last = fileName.substring(fileName.indexOf(".") + 1);
		InputStream inputStream;
		if (last.equals("xlsx")) {
			try {
				inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("xml/"+fileName);
				//定义工作簿
				XSSFWorkbook xssfWorkbook = null;
				try {
					xssfWorkbook = new XSSFWorkbook(inputStream);
				} catch (Exception e) {
					System.out.println("Excel data file cannot be found!");
				}
				if (xssfWorkbook != null) {
					//定义工作表
					XSSFSheet xssfSheet;
					xssfSheet = xssfWorkbook.getSheetAt(0);
					if (xssfSheet != null) {
						//定义行
						//默认第一行为标题行，index = 0
						XSSFRow propertyName = xssfSheet.getRow(0);
						XSSFRow propertyType = xssfSheet.getRow(1);
						XSSFRow desc = xssfSheet.getRow(2);
						//循环取每行的数据
						List<Title> titleList = new ArrayList<>();
						//循环取每个单元格(cell)的数据
						for (int cellIndex = 0; cellIndex < propertyName.getPhysicalNumberOfCells(); cellIndex++) {
							XSSFCell name = propertyName.getCell(cellIndex);
							XSSFCell type = propertyType.getCell(cellIndex);
							XSSFCell des = desc.getCell(cellIndex);
							titleList.add(new Title(getString(name), getString(type), getString(des)));
						}
						ExcelToJavaGenerator.write(javaName, titleList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (last.equals("xls")) {
			try {
				inputStream = new FileInputStream(fileName);
				//定义工作簿
				HSSFWorkbook xssfWorkbook = null;
				try {
					xssfWorkbook = new HSSFWorkbook(inputStream);
				} catch (Exception e) {
					System.out.println("Excel data file cannot be found!");
				}
				if (xssfWorkbook != null) {
					//定义工作表
					HSSFSheet xssfSheet;
					xssfSheet = xssfWorkbook.getSheetAt(0);
					if (xssfSheet != null) {
						//定义行
						//默认第一行为标题行，index = 0
						HSSFRow propertyName = xssfSheet.getRow(0);
						HSSFRow propertyType = xssfSheet.getRow(1);
						HSSFRow desc = xssfSheet.getRow(2);
						//循环取每行的数据
						List<Title> titleList = new ArrayList<>();
						//循环取每个单元格(cell)的数据
						for (int cellIndex = 0; cellIndex < propertyName.getPhysicalNumberOfCells(); cellIndex++) {
							HSSFCell name = propertyName.getCell(cellIndex);
							HSSFCell type = propertyType.getCell(cellIndex);
							HSSFCell des = desc.getCell(cellIndex);
							titleList.add(new Title(getString(name), getString(type), getString(des)));
						}
						ExcelToJavaGenerator.write(javaName, titleList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){

		readExcelCreateJavaHead("TableModel.xlsx");
	}
}