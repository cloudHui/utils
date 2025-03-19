package utils.other.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 读取xls文件的内容，并打印出来
 *
 * @author Administrator
 */
public class ReadExcel {

	/**
	 * 读取一个excel文件的内容
	 */
	public static void main(String[] args) {
		List<Map<String, String>> list = ExcelUtil.readExcel("");
		List<String> values = new ArrayList<>(list.size());
		list.forEach(map -> {
			StringBuilder sb = new StringBuilder();
			map.forEach((k, v) -> sb.append(v).append(","));
			String value = sb.toString();
			values.add(value.substring(0, value.lastIndexOf(",")).replace(".0", ""));
		});
		values.forEach(System.out::println);
	}


}