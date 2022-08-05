package utils.utils;

public class HashUtils {
    public HashUtils() {
    }

    public static int hashString(String data) {
        return null != data && !data.isEmpty() ? Math.abs(data.charAt(data.length() - 1)) : 0;
    }
}
