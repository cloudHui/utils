package music;

public class MusicParser {

    /**
     * 从网页内容解析音乐信息
     */
    public static HifiniMusic parseMusicInfo(String pageContent) {
        if (!isValidMusicPage(pageContent)) {
            return null;
        }

        HifiniMusic music = new HifiniMusic();
        try {
            String result = pageContent.trim().substring(pageContent.indexOf("music"));

            // 解析音乐名称
            String name = parseMusicName(result);
            music.setName(StringUtils.cleanMusicName(name));

            // 解析下载URL
            String downloadUrl = parseDownloadUrl(result);
            music.setDownUrl(downloadUrl);

        } catch (Exception e) {
            System.err.println("解析音乐信息失败: " + e.getMessage());
            return null;
        }

        return music;
    }

    private static boolean isValidMusicPage(String pageContent) {
        return pageContent.contains("music") &&
                pageContent.contains("title") &&
                pageContent.contains("url") &&
                pageContent.contains("pic") &&
                pageContent.contains("author");
    }

    private static String parseMusicName(String result) {
        try {
            int titleStart = result.indexOf("title") + 8;
            int authorStart = result.indexOf("author") - 1;

            if (titleStart >= 8 && authorStart > titleStart) {
                return result.substring(titleStart, authorStart);
            }
        } catch (Exception e) {
            System.err.println("解析音乐名称失败: " + e.getMessage());
        }
        return "未知歌曲";
    }

    private static String parseDownloadUrl(String result) {
        try {
            int urlStart = result.indexOf("url") + 6;
            int picStart = result.indexOf("pic") + 3;

            if (urlStart >= 6 && picStart > urlStart) {
                String urlSection = result.substring(urlStart, picStart);
                int httpsStart = urlSection.indexOf("https");
                int commaEnd = urlSection.indexOf(",") - 1;

                if (httpsStart >= 0 && commaEnd > httpsStart) {
                    return urlSection.substring(httpsStart, commaEnd);
                }
            }
        } catch (Exception e) {
            System.err.println("解析下载URL失败: " + e.getMessage());
        }
        return "";
    }
}