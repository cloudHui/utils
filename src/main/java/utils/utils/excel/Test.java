package utils.utils.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {

	public static void main(String[] args) {
		readExcel();
	}

	/**
	 * 读取Excel文件的内容
	 */
	public static void readExcel() {
		InputStream inputStream;
		Map<Integer, Integer> map = new HashMap<>();
		try {
			inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("xml/1.xlsx");
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
					//循环取每行的数据
					for (int rowIndex = 0; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
						XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
						if (xssfRow != null) {
							XSSFCell type = xssfRow.getCell(0);
							XSSFCell value = xssfRow.getCell(1);

							String[] arrayValue = getString(value).replace("[", "").replace("]", "").split(",");
							try {
								for (int index = 0, size = arrayValue.length; index < size; index++) {
									int num = Integer.parseInt(arrayValue[index]);
									if (num > 0) {
										String[] arrayType = getString(type).replace("[", "").replace("]", "").split(",");
										int typeNum = Integer.parseInt(arrayType[index]);
										map.put(typeNum, map.getOrDefault(typeNum, 0) + 1);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<TypeNum> list = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			list.add(new TypeNum(entry.getKey(), entry.getValue()));
		}

		list.sort(comparableId);
		System.out.println("id");
		list.forEach(System.out::println);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("num");
		list.sort(comparableNum);
		list.forEach(System.out::println);
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


	static Comparator<TypeNum> comparableId = Comparator.comparingInt(TypeNum::getType);

	static Comparator<TypeNum> comparableNum = Comparator.comparingInt(TypeNum::getNum);

	static class TypeNum {
		private int type;

		private int num;


		public int getType() {
			return type;
		}

		public int getNum() {
			return num;
		}

		public TypeNum(int type, int num) {
			this.type = type;
			this.num = num;
		}

		@Override
		public String toString() {
			return "TypeNum{" +
					"type=" + type +
					", num=" + num +
					'}';
		}
	}
}
