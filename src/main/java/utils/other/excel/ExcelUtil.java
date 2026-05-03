package utils.other.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Excel工具类 - 支持xls和xlsx格式的读写操作
 */
public class ExcelUtil {

    // 文件类型常量
    private static final String XLSX_EXTENSION = "xlsx";
    private static final String XLS_EXTENSION = "xls";

    /**
     * 整段为 ASCII 字母或下划线，不含数字（与表 / Sheet 命名约定一致）
     */
    private static final Pattern ASCII_IDENTIFIER_NO_DIGITS = Pattern.compile("[A-Za-z_]+");

    /**
     * Excel 默认 Sheet 名，如 Sheet1、sheet2
     */
    private static final Pattern DEFAULT_EXCEL_SHEET_NAME = Pattern.compile("(?i)Sheet\\d+");

    /**
     * 判断字符串是否为ASCII标识符（仅字母和下划线，不含数字）
     *
     * @param s 字符串
     * @return 是否为ASCII标识符
     */
    public static boolean isAsciiIdentifierNoDigits(String s) {
        return s != null && !s.isEmpty() && ASCII_IDENTIFIER_NO_DIGITS.matcher(s).matches();
    }

    /**
     * 判断Sheet名是否为默认Sheet名
     *
     * @param sheetName Sheet名
     * @return 是否为默认Sheet名
     */
    private static boolean isDefaultExcelSheetName(String sheetName) {
        return sheetName != null && DEFAULT_EXCEL_SHEET_NAME.matcher(sheetName).matches();
    }

    /**
     * Sheet 名为合法标识符；仅当首字母小写时转为公共类名形式。
     */
    private static String sheetToPublicClassName(String sheetName) {
        char c0 = sheetName.charAt(0);
        if (Character.isLowerCase(c0)) {
            return Character.toUpperCase(c0) + sheetName.substring(1);
        }
        return sheetName;
    }

    /**
     * 解析模块资源目录
     *
     * @param modulePath 模块路径
     * @return 资源目录路径
     */
    private static Path resolveResourcesDirectory(String modulePath) {
        Path ud = Paths.get(System.getProperty("user.dir"));
        Path[] candidates = new Path[]{
                ud.resolve(modulePath).resolve("src/main/resources"),
                ud.resolve("src/main/resources"),
        };
        for (Path p : candidates) {
            if (Files.isDirectory(p)) {
                return p;
            }
        }
        return candidates[0];
    }

    /**
     * 扫描模块 {@code src/main/resources} 下所有 xlsx/xls，按约定生成 {@code model.{文件名小写}} 包下的 Java 类。
     */
    public static void scanResourcesCreateJavaHead(String modulePath) {
        Path root = resolveResourcesDirectory(modulePath);
        if (!Files.isDirectory(root)) {
            System.err.println("Resources directory not found: " + root.toAbsolutePath());
            return;
        }
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                String fn = p.getFileName().toString();
                int dot = fn.lastIndexOf('.');
                if (dot < 0) {
                    return;
                }
                String stem = fn.substring(0, dot);
                String ext = fn.substring(dot + 1).toLowerCase(Locale.ROOT);
                if (!XLSX_EXTENSION.equals(ext) && !XLS_EXTENSION.equals(ext)) {
                    return;
                }
                if (!isAsciiIdentifierNoDigits(stem)) {
                    System.out.println("跳过命名不合规的 Excel: " + p.toAbsolutePath());
                    return;
                }
                processOneExcelPathForJavaHead(p, modulePath, stem, ext);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理单个Excel文件并生成Java头文件
     *
     * @param excelPath  Excel文件路径
     * @param modulePath 模块路径
     * @param stem       Excel文件名
     * @param ext        Excel文件扩展名
     */
    private static void processOneExcelPathForJavaHead(Path excelPath, String modulePath, String stem, String ext) {
        String packageSegment = stem.toLowerCase(Locale.ROOT);
        WorkbookType type = XLSX_EXTENSION.equals(ext) ? WorkbookType.XSSF : WorkbookType.HSSF;
        try (InputStream in = Files.newInputStream(excelPath)) {
            Workbook wb = create(in, type);
            if (wb != null) {
                processForJavaHead(modulePath, packageSegment, wb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel并生成Java类头文件
     */
    public static void readExcelCreateJavaHead(String fileName, String path, Class<?> aclass) {
        String fileExtension = getFileExtension(fileName);
        if (XLSX_EXTENSION.equals(fileExtension)) {
            processExcelForJavaHead(fileName, path, WorkbookType.XSSF, aclass);
        } else if (XLS_EXTENSION.equals(fileExtension)) {
            processExcelForJavaHead(fileName, path, WorkbookType.HSSF, aclass);
        }
    }

    /**
     * 读取Excel并生成Java对象数据
     */
    public static void readExcelJavaValue(String fileName, List<Object> properties, Class<?> aclass) {
        String fileExtension = getFileExtension(fileName);
        if (XLSX_EXTENSION.equals(fileExtension)) {
            processExcelForJavaValue(fileName, WorkbookType.XSSF, properties, aclass);
        } else if (XLS_EXTENSION.equals(fileExtension)) {
            processExcelForJavaValue(fileName, WorkbookType.HSSF, properties, aclass);
        }
    }

    /**
     * 写入数据到Excel
     */
    public static void writeExcel(List<List<String>> list, OutputStream outputStream) {
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

    public static void main(String[] args) {
        readExcelCreateJavaHead("TableModel.xlsx", ".", ExcelUtil.class);
    }

    /**
     * 应用程序是否为虚拟机启动
     */
    public static boolean runJar(Class<?> aclass) {
        File fromFile = new File(aclass.getProtectionDomain().getCodeSource().getLocation().getPath());
        return fromFile.isFile() && fromFile.getName().endsWith(".jar");
    }

    /**
     * 处理Excel生成Java头文件
     */
    private static void processExcelForJavaHead(String fileName, String path, WorkbookType type, Class<?> aclass) {
        String stem = fileName.substring(0, fileName.lastIndexOf('.'));
        if (!isAsciiIdentifierNoDigits(stem)) {
            System.err.println("跳过命名不合规的 Excel: " + fileName);
            return;
        }
        String packageSegment = stem.toLowerCase(Locale.ROOT);
        try (InputStream inputStream = getInputStream(fileName, aclass)) {
            Workbook processor = create(inputStream, type);
            if (processor != null) {
                processForJavaHead(path, packageSegment, processor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理Excel生成Java对象值
     */
    private static void processExcelForJavaValue(String fileName, WorkbookType type, List<Object> properties, Class<?> aclass) {
        String stem = fileName.substring(0, fileName.lastIndexOf('.'));
        if (!isAsciiIdentifierNoDigits(stem)) {
            System.err.println("跳过命名不合规的 Excel: " + fileName);
            return;
        }
        String packageSegment = stem.toLowerCase(Locale.ROOT);
        try (InputStream inputStream = getInputStream(fileName, aclass)) {
            Workbook processor = create(inputStream, type);
            if (processor != null) {
                processForJavaValue(packageSegment, processor, properties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件输入流（开发态在 tool/src/main/resources 下递归查找；优先 xml 子目录）
     */
    private static InputStream getInputStream(String fileName, Class<?> aclass) throws Exception {
        Path path = findXlsxPath(fileName, aclass);
        if (path == null) {
            throw new IOException("Excel file not found: " + fileName);
        }
        return Files.newInputStream(path);
    }

    /**
     * 查找Excel文件路径
     *
     * @param fileName Excel文件名
     * @param aclass   类
     * @return Path 文件路径
     */
    private static Path findXlsxPath(String fileName, Class<?> aclass) {
        if (runJar(aclass)) {
            Path p = Paths.get(fileName);
            return Files.isRegularFile(p) ? p : null;
        }
        String ud = System.getProperty("user.dir");
        Path[] xmlShortcuts = new Path[]{
                Paths.get(ud, "tool", "src", "main", "resources", "xml", fileName),
                Paths.get(ud, "src", "main", "resources", "xml", fileName),
        };
        for (Path p : xmlShortcuts) {
            if (Files.isRegularFile(p)) {
                return p;
            }
        }
        Path[] roots = new Path[]{
                Paths.get(ud, "tool", "src", "main", "resources"),
                Paths.get(ud, "src", "main", "resources"),
        };
        for (Path root : roots) {
            if (!Files.isDirectory(root)) {
                continue;
            }
            // 遍历根目录下的所有文件和子目录
            try (Stream<Path> walk = Files.walk(root)) {
                Optional<Path> found = walk
                        .filter(Files::isRegularFile)
                        .filter(f -> fileName.equalsIgnoreCase(f.getFileName().toString()))
                        .findFirst();
                if (found.isPresent()) {
                    return found.get();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
            XSSFCell xssfCell = (XSSFCell) cell;
            cellType = xssfCell.getCellType();
            cellValue = xssfCell;
        } else if (cell instanceof HSSFCell) {
            HSSFCell hssfCell = (HSSFCell) cell;
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
            return ((XSSFCell) cell).getNumericCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell) cell).getNumericCellValue();
        return 0;
    }

    // 布尔值获取方法
    private static boolean getBooleanValue(Object cell) {
        if (cell instanceof XSSFCell)
            return ((XSSFCell) cell).getBooleanCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell) cell).getBooleanCellValue();
        return false;
    }

    // 字符串值获取方法
    private static String getStringValue(Object cell) {
        if (cell instanceof XSSFCell)
            return ((XSSFCell) cell).getStringCellValue();
        if (cell instanceof HSSFCell)
            return ((HSSFCell) cell).getStringCellValue();
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
            return (Class<?>) typeField.get(null);
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

    public static void processSheetForJavaHead(Sheet sheet, String path, String packageSegment) {
        String sheetName = sheet.getSheetName();
        if (isDefaultExcelSheetName(sheetName) || !isAsciiIdentifierNoDigits(sheetName)) {
            return;
        }
        String javaClassName = sheetToPublicClassName(sheetName);
        Row propertyName = sheet.getRow(0);
        Row propertyType = sheet.getRow(1);
        Row desc = sheet.getRow(2);
        if (propertyName == null || propertyType == null || desc == null) {
            return;
        }

        List<Title> titleList = new ArrayList<>();
        for (int cellIndex = 0; cellIndex < propertyName.getPhysicalNumberOfCells(); cellIndex++) {
            String name = getCellValue(propertyName.getCell(cellIndex));
            String type = getCellValue(propertyType.getCell(cellIndex));
            String description = getCellValue(desc.getCell(cellIndex));
            titleList.add(new Title(name, type, description));
        }

        try {
            ExcelToJavaGenerator.write(javaClassName, path, packageSegment, titleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void processForJavaHead(String path, String packageSegment, Workbook workbook) {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet != null) {
                processSheetForJavaHead(sheet, path, packageSegment);
            }
        }
    }

    public static void processForJavaValue(String packageSegment, Workbook workbook, List<Object> properties) {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            Sheet sheet = workbook.getSheetAt(index);
            if (sheet == null) {
                continue;
            }
            String sheetName = sheet.getSheetName();
            if (isDefaultExcelSheetName(sheetName) || !isAsciiIdentifierNoDigits(sheetName)) {
                continue;
            }
            String className = sheetToPublicClassName(sheetName);
            Row propertyName = sheet.getRow(0);
            Row propertyType = sheet.getRow(1);
            if (propertyName == null || propertyType == null) {
                continue;
            }
            Object obj;
            for (int rowIndex = 3; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    obj = createObjectByName("model." + packageSegment + "." + className);
                    if (obj == null) {
                        System.err.println("processForJavaValue error " + packageSegment + "." + className);
                        continue;
                    }
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
                    properties.add(obj);
                }
            }
        }
    }
}
