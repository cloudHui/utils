package music;

import java.awt.*;

public class Constants {
    // 网络相关常量
    public static final String BASE_URL = "https://www.hifini.com.cn/";
    public static final String SEARCH_URL_PREFIX = "https://www.hifini.com.cn/search-";
    public static final String DOWNLOAD_PATH = "D:/BaiduNetdiskDownload/music/";
    public static final int BUFFER_SIZE = 8192;

    // 文件类型常量
    public static final String MP3 = "mp3";
    public static final String M4A = "m4a"; // 注意：这里可能是错误，M4A应该是m4a
    public static final String FLAC = "flac";

    // 颜色常量
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color ERROR_COLOR = new Color(231, 76, 60);
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    public static final Color PANEL_BG = new Color(255, 255, 255);
    public static final Color INFO_COLOR = new Color(241, 196, 15);

    // 字体常量
    public static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 18);
    public static final Font BUTTON_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    public static final Font LABEL_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    public static final Font TABLE_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);

    // 表格列名
    public static final String[] TABLE_COLUMN_NAMES = {"选择", "歌曲名称", "下载地址", "状态"};

    // 表格列宽配置
    public static final int COLUMN_WIDTH_SELECT = 60;
    public static final int COLUMN_WIDTH_NAME = 250;
    public static final int COLUMN_WIDTH_URL = 400;
    public static final int COLUMN_WIDTH_STATUS = 100;
    public static final int ROW_HEIGHT = 35;

    private Constants() {
        // 防止实例化
    }
}