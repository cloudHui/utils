package music;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkUtils {

    private NetworkUtils() {
        // 防止实例化
    }

    /**
     * 下载网页内容
     */
    public static String downloadWebPage(String urlString) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        URL url = new URL(urlString);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }

        return contentBuilder.toString();
    }

    /**
     * 下载文件
     */
    public static boolean downloadFile(HifiniMusic music, String savePath) {
        try {
            URL url = new URL(music.getDownUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream fos = new FileOutputStream(savePath)) {

                byte[] buffer = new byte[Constants.BUFFER_SIZE];
                int bytesRead;

                while ((bytesRead = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            connection.disconnect();
            return true;

        } catch (Exception e) {
            System.err.println("文件下载失败: " + music.getName() + " - " + e.getMessage());
            return false;
        }
    }
}