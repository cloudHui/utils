package utils.other.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具类 - 支持xls和xlsx格式的读写操作
 */
public class ExcelUtil {

    // 文件类型常量
    private static final String XLSX_EXTENSION = "xlsx";
    private static final String XLS_EXTENSION = "xls";

    /**
     * 创建Excel工作簿并设置标题
     */
    public static void setHSSFWorkbookTitle(String sheetName, String[] title, HSSFWorkbook wb) {
        HSSFWorkbook workbook = (wb == null) ? new HSSFWorkbook() : wb;
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < title.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * 向Sheet中添加内容
     */
    public static void addContent(HSSFSheet sheet, String[][] values) {
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i < values.length; i++) {
            HSSFRow rowContent = sheet.createRow(lastRowNum + i + 1);
            for (int j = 0; j < values[i].length; j++) {
                rowContent.createCell(j).setCellValue(values[i][j]);
            }
        }
    }

    /**
     * 输出Excel文件
     */
    public static void outFile(HSSFWorkbook workbook, String path) {
        try (OutputStream os = new FileOutputStream(path)) {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel并生成Java类头文件
     */
    public static void readExcelCreateJavaHead(String fileName, String path) {
        String fileExtension = getFileExtension(fileName);
        String javaName = ExcelToJavaGenerator.capitalize(fileName.split("\\.")[0]);

        if (XLSX_EXTENSION.equals(fileExtension)) {
            processExcelForJavaHead(fileName, path, javaName, WorkbookType.XSSF);
        } else if (XLS_EXTENSION.equals(fileExtension)) {
            processExcelForJavaHead(fileName, path, javaName, WorkbookType.HSSF);
        }
    }

    /**
     * 读取Excel并生成Java对象数据
     */
    public static void readExcelJavaValue(String fileName) {
        String fileExtension = getFileExtension(fileName);
        String javaName = ExcelToJavaGenerator.capitalize(fileName.split("\\.")[0]);

        if (XLSX_EXTENSION.equals(fileExtension)) {
            processExcelForJavaValue(fileName, javaName, WorkbookType.XSSF);
        } else if (XLS_EXTENSION.equals(fileExtension)) {
            processExcelForJavaValue(fileName, javaName, WorkbookType.HSSF);
        }
    }

    /**
     * 写入数据到Excel
     */
    public static void writeExcel(List<List> list, OutputStream outputStream) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();

            for (int i = 0; i < list.size(); i++) {
                XSSFRow row = sheet.createRow(i);
                List<String> subList = list.get(i);

                for (int j = 0; j < subList.size(); j++) {
                    row.createCell(j).setCellValue(subList.get(j));
                }
            }

            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================ 私有方法 ================

    /**
     * 处理Excel生成Java头文件
     */
    private static void processExcelForJavaHead(File fileName, String path, String javaName, WorkbookType type) {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            Workbook webHook = create(inputStream, type);
            if (webHook != null) {
                processForJavaHead(path, javaName, webHook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readExcelCreateJavaHead("TableModel.xlsx", ".");
    }

    /**
     * 应用程序是否为虚拟机启动
     */
    public static boolean runJar() {
        File fromFile = new File(ExcelUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return fromFile.isFile() && fromFile.getName().endsWith(".jar");
    }

    /**
     * 处理Excel生成Java头文件
     */
    private static void processExcelForJavaHead(String fileName, String path, String javaName, WorkbookType type) {
        try (InputStream inputStream = getInputStream(fileName)) {
            Workbook processor = create(inputStream, type);
            if (processor != null) {
                processForJavaHead(path, javaName, processor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理Excel生成Java对象值
     */
    private static void processExcelForJavaValue(String fileName, String javaName, WorkbookType type) {
        try (InputStream inputStream = getInputStream(fileName)) {
            Workbook processor = create(inputStream, type);
            if (processor != null) {
                processForJavaValue(javaName, processor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件输入流
     */
    private static InputStream getInputStream(String fileName) throws FileNotFoundException {
        if (runJar()) {
            return new FileInputStream(fileName);
        } else {
            return new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\xml\\" + fileName);
        }
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 单元格内容转为字符串
     */
    private static String getCellValue(Object cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType;
        Object cellValue;

        if (cell instanceof XSSFCell) {
            XSSFCell xssfCell = (XSSFCell)cell;
            cellType = xssfCell.getCellType();
            cellValue = xssfCell;
        } else if (cell instanceof HSSFCell) {
            HSSFCell hssfCell = (HSSFCell)cell;
            cellType = hssfCell.getCellType();
            cellValue = hssfCell;
        } else {
            return "";
        }

        return convertCellValue(cellType, cellValue);
    }

    /**
     * 根据单元格类型转换值
     */
    private static String convertCellValue(CellType cellType, Object cell) {
        switch (cellType) {
            case NUMERIC:
                return String.valueOf(getNumericValue(cell));
            case BOOLEAN:
                return String.valueOf(getBooleanValue(cell));
            default:
                return getStringValue(cell);
        }
    }

    // 数值获取方法
    private static double getNumericValue(Object cell) {
        if (cell instanceof XSSFCell)
            return ((XSSFCell)cell).getNumericCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell)cell).getNumericCellValue();
        return 0;
    }

    // 布尔值获取方法
    private static boolean getBooleanValue(Object cell) {
        if (cell instanceof XSSFCell)
            return ((XSSFCell)cell).getBooleanCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell)cell).getBooleanCellValue();
        return false;
    }

    // 字符串值获取方法
    private static String getStringValue(Object cell) {
        if (cell instanceof XSSFCell)
            return ((XSSFCell)cell).getStringCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell)cell).getStringCellValue();
        return "";
    }

    // ================ 反射相关方法 ================

    /**
     * 调用对象的setter方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) throws Exception {
        Class<?> clazz = obj.getClass();
        String methodName = "set" + ExcelToJavaGenerator.capitalize(propertyName);
        Method method = clazz.getMethod(methodName, getUnboxedTypeGeneric(value));
        method.invoke(obj, value);
    }

    /**
     * 获取基本类型
     */
    public static Class<?> getUnboxedTypeGeneric(Object wrappedInstance) {
        if (wrappedInstance == null) {
            throw new IllegalArgumentException("Wrapped instance cannot be null.");
        }

        try {
            Field typeField = wrappedInstance.getClass().getField("TYPE");
            return (Class<?>)typeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return wrappedInstance.getClass();
        }
    }

    /**
     * 根据类名创建对象实例
     */
    public static Object createObjectByName(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            System.err.println("Failed to create object for class: " + className);
            e.printStackTrace();
            return null;
        }
    }

    public static String getJavaName(String sheetName, String javaName) {
        if (!sheetName.contains("sheet")) {
            return sheetName.substring(0, 1).toUpperCase() + sheetName.substring(1);
        }
        return javaName;
    }

    // ================ 内部枚举和类 ================

    /**
     * 工作簿类型枚举
     */
    private enum WorkbookType {
        XSSF, HSSF
    }

    /**
     * 工作簿处理器抽象类
     */
    public static Workbook create(InputStream inputStream, WorkbookType type) {
        try {
            switch (type) {
                case XSSF:
                    return new XSSFWorkbook(inputStream);
                case HSSF:
                    return new HSSFWorkbook(inputStream);
                default:
                    return null;
            }
        } catch (IOException e) {
            System.out.println("Excel data file cannot be found!");
            return null;
        }
    }

    public static void processSheetForJavaHead(Sheet sheet, String path, String javaName) {
        Row propertyName = sheet.getRow(0);
        Row propertyType = sheet.getRow(1);
        Row desc = sheet.getRow(2);

        List<Title> titleList = new ArrayList<>();
        for (int cellIndex = 0; cellIndex < propertyName.getPhysicalNumberOfCells(); cellIndex++) {
            String name = getCellValue(propertyName.getCell(cellIndex));
            String type = getCellValue(propertyType.getCell(cellIndex));
            String description = getCellValue(desc.getCell(cellIndex));
            titleList.add(new Title(name, type, description));
        }

        String finalJavaName = getJavaName(sheet.getSheetName(), javaName);

        try {
            ExcelToJavaGenerator.write(finalJavaName, path, titleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData(Workbook workbook) {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet != null) {
                Row titleRow = sheet.getRow(0);

                for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                            String title = getCellValue(titleRow.getCell(cellIndex));
                            String value = getCellValue(row.getCell(cellIndex));
                        }
                    }
                }
            }
        }
    }

    public static void processForJavaHead(String path, String javaName, Workbook workbook) {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet != null) {
                processSheetForJavaHead(sheet, path, javaName);
            }
        }
    }

    public static void processForJavaValue(String javaName, Workbook workbook) {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet != null) {
                Row propertyName = sheet.getRow(0);
                Row propertyType = sheet.getRow(1);
                Object obj;
                for (int rowIndex = 3; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        obj = createObjectByName("model." + getJavaName(sheet.getSheetName(), javaName));
                        for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                            String name = getCellValue(propertyName.getCell(cellIndex));
                            String type = getCellValue(propertyType.getCell(cellIndex));
                            String value = getCellValue(row.getCell(cellIndex));

                            try {
                                invokeSetter(obj, name, ExcelToJavaGenerator.getType(type, value));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
