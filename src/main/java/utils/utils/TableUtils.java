package utils.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class TableUtils {
	private static TableUtils instance = new TableUtils();

	private TableUtils() {
	}

	public static TableUtils getInstance() {
		return instance;
	}

	public String getString(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (String) (map.get(key));
		}
		return null;
	}

	public float getFloat(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (float) (map.get(key));
		}
		return -1;
	}

	public int getInt(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (int) (map.get(key));
		}
		return -1;
	}

	public long getLong(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (long) (map.get(key));
		}
		return -1;
	}

	public Boolean getBoolean(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (Boolean) (map.get(key));
		}
		return false;
	}

	//public Vector2 getVector2(HashMap<String, Object> map, String key) {
	//	if (map.containsKey(key)) {
	//		return (Vector2) (map.get(key));
	//	}
	//	logger.error("no found table key: " + key);
	//	return null;
	//}
	//
	//public Vector3 getVector3(HashMap<String, Object> map, String key) {
	//	if (map.containsKey(key)) {
	//		return (Vector3) (map.get(key));
	//	}
	//	logger.error("no found table key: " + key);
	//	return null;
	//}

	public Timestamp getTimestamp(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (Timestamp) (map.get(key));
		}
		return null;
	}

	public String[] getStringArray(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (String[]) (map.get(key));
		}
		return null;
	}

	public int[] getIntArray(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (int[]) (map.get(key));
		}
		return null;
	}

	public long[] getLongArray(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (long[]) (map.get(key));
		}
		return null;
	}

	public float[] getFloatArray(HashMap<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (float[]) (map.get(key));
		}
		return null;
	}

	//public Vector2[] getVector2Array(HashMap<String, Object> map, String key) {
	//	if (map.containsKey(key)) {
	//		return (Vector2[]) (map.get(key));
	//	}
	//	logger.error("no found table key: " + key);
	//	return null;
	//}
	//
	//public Vector3[] getVector3Array(HashMap<String, Object> map, String key) {
	//	if (map.containsKey(key)) {
	//		return (Vector3[]) (map.get(key));
	//	}
	//	logger.error("no found table key: " + key);
	//	return null;
	//}

	public int[][] getIntArrayArray(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (int[][]) (map.get(key));
		}
		return null;
	}

	public float[][] getFloatArrayArray(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			return (float[][]) (map.get(key));
		}
		return null;
	}

	//public static Vector2 Vector2Parse(String value) {
	//	Vector2 vector2 = new Vector2();
	//	float[] array = FloatArrayParse(value);
	//	vector2.x = array[0];
	//	vector2.y = array[1];
	//	return vector2;
	//}
	//
	//public static Vector3 Vector3Parse(String value) {
	//	Vector3 vector3 = new Vector3();
	//	float[] array = FloatArrayParse(value);
	//	vector3.x = array[0];
	//	vector3.y = array[1];
	//	vector3.z = array[2];
	//	return vector3;
	//}

	public static String StringArrayToString(List<String> arrayStr) {
		StringBuilder value = new StringBuilder();
		for (int i = 0; i < arrayStr.size(); i++) {
			if (i == 0) {
				value = new StringBuilder(arrayStr.get(i));
			} else {
				value.append(",").append(arrayStr.get(i));
			}
		}
		return value.toString();
	}

	public static String[] StringArrayParse(String value) {
		value = value.trim();
		value = value.replace("(", "");
		value = value.replace(")", "");
		value = value.replace("[", "");
		value = value.replace("]", "");
		if (value.isEmpty()) {
			return new String[0];
		}
		return value.split(",");
	}

	public static Timestamp TimeStampParse(String value) {
		Timestamp timestamp = Timestamp.valueOf(value);
		if (TimeUtil.logicTimeZone.equals("GMT+8")) {
			TimeZone curTimeZone = TimeZone.getDefault();
			int offset = curTimeZone.getOffset(System.currentTimeMillis());
			long realTime = timestamp.getTime() - (8 * TimeUtil.HOUR - offset);
			if (realTime < 0) {
				realTime = 0;
			}
			return new Timestamp(realTime);
		} else {
			TimeZone curTimeZone = TimeZone.getDefault();
			int offset = curTimeZone.getOffset(System.currentTimeMillis());
			long realTime = timestamp.getTime() - (-offset);
			if (realTime < 0) {
				realTime = 0;
			}
			return new Timestamp(realTime);
		}
	}

	public static int[] IntArrayParse(String value) {
		String[] intStrArray = StringArrayParse(value);
		int[] intArray = new int[intStrArray.length];
		for (int i = 0; i < intStrArray.length; i++) {
			intArray[i] = Integer.parseInt(intStrArray[i]);
		}
		return intArray;
	}

	public static String IntArrayToString(int[] intArray) {
		if (intArray.length > 0) {
			StringBuilder stringBuffer = new StringBuilder();
			stringBuffer.append("[");
			for (int i = 0; i < intArray.length; i++) {
				if (i != 0)
					stringBuffer.append(",");
				stringBuffer.append(intArray[i]);
			}
			stringBuffer.append("]");
			return stringBuffer.toString();
		}
		return "";
	}

	public static List<Integer> IntegerListParse(String str) {
		List<Integer> list = new ArrayList<>();
		if (str.isEmpty()) {
			return list;
		}
		String[] strArray = StringArrayParse(str);
		for (String s : strArray) list.add(Integer.parseInt(s));
		return list;
	}

	public static String IntegerListToString(List<Integer> list) {
		if (list.isEmpty())
			return "";
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append("[");
		for (int i = 0; i < list.size(); i++) {
			if (i != 0)
				stringBuffer.append(",");
			stringBuffer.append(list.get(i));
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	//public static List<Vector2> Vector2ListParse(String str) {
	//	String[] strArray = ReFormatString(str);
	//	List<Vector2> list = new ArrayList<>();
	//	for (int i = 0; i < strArray.length; i++) {
	//		if (StrUtil.isEmpty(strArray[i])) {
	//			continue;
	//		}
	//		list.add(Vector2Parse(strArray[i]));
	//	}
	//	return list;
	//}
	//
	//public static List<Vector3> Vector3ListParse(String str) {
	//	String[] strArray = ReFormatString(str);
	//	List<Vector3> list = new ArrayList<>();
	//	for (int i = 0; i < strArray.length; i++) {
	//		if (StrUtil.isEmpty(strArray[i])) {
	//			continue;
	//		}
	//		list.add(Vector3Parse(strArray[i]));
	//	}
	//	return list;
	//}
	//
	//public static String Vector2ListToString(List<Vector2> list) {
	//	if (list.isEmpty())
	//		return "";
	//	StringBuffer stringBuffer = new StringBuffer();
	//	stringBuffer.append("[");
	//	for (Vector2 vector2 : list) {
	//		if (stringBuffer.charAt(stringBuffer.length() - 1) == ')') {
	//			stringBuffer.append(",");
	//		}
	//		stringBuffer.append("(").append(vector2.x).append(",").append(vector2.y).append(")");
	//	}
	//	stringBuffer.append("]");
	//	return stringBuffer.toString();
	//}
	//
	//public static String Vector3ListToString(List<Vector3> list) {
	//	if (list.isEmpty())
	//		return "";
	//	StringBuffer stringBuffer = new StringBuffer();
	//	stringBuffer.append("[");
	//	for (Vector3 vector3 : list) {
	//		if (stringBuffer.charAt(stringBuffer.length() - 1) == ')') {
	//			stringBuffer.append(",");
	//		}
	//		stringBuffer.append("(").append(vector3.x).append(",").append(vector3.y).append(",").append(vector3.z).append(")");
	//	}
	//	stringBuffer.append("]");
	//	return stringBuffer.toString();
	//}

	public static long[] LongArrayParse(String value) {
		String[] longStrArray = StringArrayParse(value);
		long[] longArray = new long[longStrArray.length];
		for (int i = 0; i < longStrArray.length; i++) {
			longArray[i] = Long.parseLong(longStrArray[i]);
		}
		return longArray;
	}

	public static String LongArrayToString(long[] longArray) {
		if (longArray.length > 0) {
			StringBuilder stringBuffer = new StringBuilder();
			stringBuffer.append("[");
			for (int i = 0; i < longArray.length; i++) {
				if (i != 0)
					stringBuffer.append(",");
				stringBuffer.append(longArray[i]);
			}
			stringBuffer.append("]");
			return stringBuffer.toString();
		}
		return "";
	}

	public static float[] FloatArrayParse(String value) {
		String[] floatStrArray = StringArrayParse(value);
		float[] floatArray = new float[floatStrArray.length];
		for (int i = 0; i < floatStrArray.length; i++) {
			floatArray[i] = Float.parseFloat(floatStrArray[i]);
		}
		return floatArray;
	}

	//public static Vector2[] Vector2ArrayParse(String value) {
	//	String[] vector2StrArray = ReFormatString(value);
	//	Vector2[] vector2Array = new Vector2[vector2StrArray.length];
	//	for (int i = 0; i < vector2StrArray.length; i++) {
	//		vector2Array[i] = Vector2Parse(vector2StrArray[i]);
	//	}
	//	return vector2Array;
	//}
	//
	//public static Vector3[] Vector3ArrayParse(String value) {
	//	String[] vector3StrArray = ReFormatString(value);
	//	Vector3[] vector3Array = new Vector3[vector3StrArray.length];
	//	for (int i = 0; i < vector3StrArray.length; i++) {
	//		vector3Array[i] = Vector3Parse(vector3StrArray[i]);
	//	}
	//	return vector3Array;
	//}

	public static Timestamp[] TimestampArrayParse(String value) {
		String[] timestampStrArray = ReFormatString(value);
		Timestamp[] timestampArray = new Timestamp[timestampStrArray.length];
		for (int i = 0; i < timestampArray.length; i++) {
			timestampArray[i] = TimeStampParse(timestampStrArray[i]);
		}
		return timestampArray;
	}

	public static int[][] IntArrayArrayParse(String value) {
		String[] intArrayStrArray = ReFormatString(value);
		int[][] intArrayArray = new int[intArrayStrArray.length][];
		for (int i = 0; i < intArrayStrArray.length; i++) {
			intArrayArray[i] = IntArrayParse(intArrayStrArray[i]);
		}
		return intArrayArray;
	}

	public static long[][] LongArrayArrayParse(String value) {
		String[] intArrayStrArray = ReFormatString(value);
		long[][] intArrayArray = new long[intArrayStrArray.length][];
		for (int i = 0; i < intArrayStrArray.length; i++) {
			intArrayArray[i] = LongArrayParse(intArrayStrArray[i]);
		}
		return intArrayArray;
	}

	public static float[][] FloatArrayArrayParse(String value) {
		String[] floatArrayStrArray = ReFormatString(value);
		float[][] floatArrayArray = new float[floatArrayStrArray.length][];
		for (int i = 0; i < floatArrayStrArray.length; i++) {
			floatArrayArray[i] = FloatArrayParse(floatArrayStrArray[i]);
		}
		return floatArrayArray;
	}

	public static String[][] StringArrayArrayParse(String value) {
		String[] strArray = ReFormatString(value);
		String[][] arrayArray = new String[strArray.length][];
		for (int i = 0; i < strArray.length; i++) {
			arrayArray[i] = StringArrayParse(strArray[i]);
		}
		return arrayArray;
	}

	//public static String vector3ToString(List<Vector3> vector3List) {
	//	if (vector3List.size() > 0) {
	//		StringBuilder stringBuffer = new StringBuilder();
	//		stringBuffer.append("[");
	//		for (Vector3 vector3 : vector3List) {
	//			if (stringBuffer.charAt(stringBuffer.length() - 1) == ')') {
	//				stringBuffer.append(",");
	//			}
	//			stringBuffer.append("(").append(vector3.x).append(",").append(vector3.y).append(",").append(vector3.z).append(")");
	//		}
	//		stringBuffer.append("]");
	//		return stringBuffer.toString();
	//	}
	//	return null;
	//}

	public static String listToString(List<Long> list) {
		if (list.size() > 0) {
			StringBuilder stringBuffer = new StringBuilder();
			for (Object id : list) {
				stringBuffer.append(id).append(",");
			}
			return stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
		}
		return null;
	}

	private static String[] ReFormatString(String value) {
		if (value.contains("(")) {
			value = value.trim();
			value = value.replace("[", "");
			value = value.replace("]", "");
			value = value.replace("),", ")|");
			value = value.replace("(", "");
			value = value.replace(")", "");
			if (value.isEmpty()) {
				return new String[0];
			}
			return value.split("\\|");
		} else {
			value = value.trim();
			value = value.replace("[[", "");
			value = value.replace("]]", "");
			value = value.replace("],", "]|");
			value = value.replace("[", "");
			value = value.replace("]", "");
			if (value.isEmpty()) {
				return new String[0];
			}
			return value.split("\\|");
		}
	}

	public static List<Long> LongListParse(String str) {
		List<Long> list = new ArrayList<>();
		if (str.isEmpty()) {
			return list;
		}
		String[] strArray = StringArrayParse(str);
		for (String s : strArray) list.add(Long.parseLong(s));
		return list;
	}

	public static String LongListToString(List<Long> list) {
		if (list.isEmpty())
			return "";
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append("[");
		for (int i = 0; i < list.size(); i++) {
			if (i != 0)
				stringBuffer.append(",");
			stringBuffer.append(list.get(i));
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	public static List<String> filterEmptyString(List<String> inList) {
		List<String> list = new ArrayList<>();
		for (String element : inList) {
			if (element.isEmpty()) {
				continue;
			}
			list.add(element);
		}
		return list;
	}

	public static List<String> filterEmptyString(String[] inArray) {
		return filterEmptyString(Arrays.asList(inArray));
	}

	public static ArrayList<Integer> arrayToList(int[] value) {
		ArrayList<Integer> arrayToList = new ArrayList<>();
		for (int j : value) {
			arrayToList.add(j);
		}
		return arrayToList;
	}

	public static int[] listToArray(List<Integer> value) {
		int[] listToArray = new int[value.size()];
		for (int i = 0; i < value.size(); i++) {
			listToArray[i] = value.get(i);
		}
		return listToArray;
	}

	public static List<Integer> IntListVersionParse(String value) {
		List<Integer> intList = new ArrayList<>();
		if (value == null || value.equals("[]") || value.equals(""))
			return intList;
		String[] intStrArray = value.split("\\.");
		for (String s : intStrArray) {
			intList.add(Integer.valueOf(s));
		}
		return intList;
	}

	public static List<Integer> IntListParse(String value) {
		List<Integer> intList = new ArrayList<>();
		if (value == null || value.equals("[]") || value.equals(""))
			return intList;
		String[] intStrArray = StringArrayParse(value);
		for (String s : intStrArray) {
			intList.add(Integer.parseInt(s));
		}
		return intList;
	}

	////[(101.0,10.0,0.0)],
	//// [(303.0,30.0,0.0),(404.0,40.0,0.0),(505.0,50.0,0.0)],
	//// [(404.0,40.0,0.0),(505.0,50.0,0.0),(606.0,60.0,0.0)]
	//public static List<String> stringToStringVectorsList(String value) {
	//	List<String> list = new ArrayList<>();
	//	if (value == null || value.isEmpty()) {
	//		return list;
	//	}
	//	value = value.trim();
	//	String[] arrays = value.split(CommonConst.RIGHT_BRACKETS);
	//	for (String temp : arrays) {
	//		if (CommonConst.ARRAY_SEPARATOR.equals(String.valueOf(temp.charAt(0)))) {
	//			temp = temp.substring(1);
	//		}
	//		list.add(temp + CommonConst.RIGHT_BRACKETS);
	//	}
	//	return list;
	//}

	////二维数组转string
	//public static String twoDimensionalIntArrayToString(int[][] intArray) {
	//	if (intArray.length > 0) {
	//		StringBuilder builder = new StringBuilder().append(CommonConst.LEFT_BRACKETS);
	//		for (int abscissa = 0, abscissaLength = intArray.length; abscissa < abscissaLength; abscissa++) {
	//			int[] array = intArray[abscissa];
	//			builder.append(CommonConst.LEFT_PARENTHESES);
	//			for (int ordinate = 0, ordinateLength = array.length; ordinate < ordinateLength; ordinate++) {
	//				if (ordinate != 0) {
	//					builder.append(CommonConst.ARRAY_SEPARATOR);
	//				}
	//				builder.append(array[ordinate]);
	//			}
	//			builder.append(CommonConst.RIGHT_PARENTHESES);
	//			if (abscissa != abscissaLength - 1) {
	//				builder.append(CommonConst.ARRAY_SEPARATOR);
	//			}
	//		}
	//		builder.append(CommonConst.RIGHT_BRACKETS);
	//		return builder.toString();
	//	}
	//	return "";
	//}

	////二维数组转string
	//public static String twoDimensionalLongArrayToString(long[][] intArray) {
	//	if (intArray.length > 0) {
	//		StringBuilder builder = new StringBuilder().append(CommonConst.LEFT_BRACKETS);
	//		for (int abscissa = 0, abscissaLength = intArray.length; abscissa < abscissaLength; abscissa++) {
	//			long[] array = intArray[abscissa];
	//			builder.append(CommonConst.LEFT_PARENTHESES);
	//			for (int ordinate = 0, ordinateLength = array.length; ordinate < ordinateLength; ordinate++) {
	//				if (ordinate != 0) {
	//					builder.append(CommonConst.ARRAY_SEPARATOR);
	//				}
	//				builder.append(array[ordinate]);
	//			}
	//			builder.append(CommonConst.RIGHT_PARENTHESES);
	//			if (abscissa != abscissaLength - 1) {
	//				builder.append(CommonConst.ARRAY_SEPARATOR);
	//			}
	//		}
	//		builder.append(CommonConst.RIGHT_BRACKETS);
	//		return builder.toString();
	//	}
	//	return "";
	//}


	public static List<long[]> listLongArrayArrayParse(String value) {
		String[] intArrayStrArray = ReFormatString(value);
		List<long[]> list = new ArrayList<>(intArrayStrArray.length);
		for (String s : intArrayStrArray) {
			list.add(LongArrayParse(s));
		}
		return list;
	}

	//public static String listToTwoLongArray(List<long[]> list) {
	//	if (!list.isEmpty()) {
	//		StringBuilder builder = new StringBuilder().append(CommonConst.LEFT_BRACKETS);
	//		for (int abscissa = 0, abscissaLength = list.size(); abscissa < abscissaLength; abscissa++) {
	//			long[] array = list.get(abscissa);
	//			builder.append(CommonConst.LEFT_PARENTHESES);
	//			for (int ordinate = 0, ordinateLength = array.length; ordinate < ordinateLength; ordinate++) {
	//				if (ordinate != 0) {
	//					builder.append(CommonConst.ARRAY_SEPARATOR);
	//				}
	//				builder.append(array[ordinate]);
	//			}
	//			builder.append(CommonConst.RIGHT_PARENTHESES);
	//			if (abscissa != abscissaLength - 1) {
	//				builder.append(CommonConst.ARRAY_SEPARATOR);
	//			}
	//		}
	//		builder.append(CommonConst.RIGHT_BRACKETS);
	//		return builder.toString();
	//	}
	//	return "";
	//}

	//public static Map<Float, Float> stringToMapParse(String str) {
	//	String[] strArray = ReFormatString(str);
	//	Map<Float, Float> map = new HashMap<>();
	//	for (int i = 0; i < strArray.length; i++) {
	//		if (StrUtil.isEmpty(strArray[i])) {
	//			continue;
	//		}
	//		float[] array = FloatArrayParse(strArray[i]);
	//		map.put(array[0], array[1]);
	//	}
	//	return map;
	//}

	public static String mapToString(Map<Float, Float> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append("[");
		for (Map.Entry<Float, Float> entry : map.entrySet()) {
			if (stringBuffer.charAt(stringBuffer.length() - 1) == ')') {
				stringBuffer.append(",");
			}
			stringBuffer.append("(").append(entry.getKey()).append(",").append(entry.getValue()).append(")");
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
}
