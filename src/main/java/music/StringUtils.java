package music;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private StringUtils() {
        // 防止实例化
    }

    /**
     * 提取两个字符之间的内容
     */
    public static List<String> extractBetweenChars(String input, String startChar, String endChar) {
        List<String> results = new ArrayList<>();
        String regex = Pattern.quote(startChar) + "(.*?)" + Pattern.quote(endChar);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            results.add(matcher.group());
        }

        return results;
    }

    /**
     * 缩短URL显示
     */
    public static String shortenUrl(String url) {
        if (url.length() > 50) {
            return url.substring(0, 30) + "..." + url.substring(url.length() - 20);
        }
        return url;
    }

    /**
     * 清理音乐名称
     */
    public static String cleanMusicName(String name) {
        if (name == null) return "";
        return name.trim()
                .replace("\"", "")
                .replace(",", "");
    }
}