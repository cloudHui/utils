// MusicDownloader.java (完整版)
package music;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicDownloader extends JFrame {
    // UI组件
    private JTextField searchField;
    private JTextField directUrlField;
    private JButton searchButton;
    private JButton downloadButton;
    private JButton selectAllButton;
    private JButton clearAllButton;
    private JButton directDownloadButton;
    private JTable resultTable;
    private MusicTableModel tableModel;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel titleLabel;

    // 数据
    private final List<HifiniMusic> musicList = new ArrayList<>();
    private final Set<HifiniMusic> selectedMusics = new HashSet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public MusicDownloader() {
        initComponents();
        setupLayout();
        setupListeners();
        applyStyles();
        setupWindow();
    }

    // 初始化方法组（每个方法都在50行以内）
    private void initComponents() {
        titleLabel = new JLabel("HiFi音乐下载器");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        searchField = new JTextField();
        searchField.setToolTipText("请输入歌曲名称进行搜索");

        directUrlField = new JTextField();
        directUrlField.setToolTipText("直接输入音乐文件URL进行下载");

        searchButton = new JButton("🔍 搜索");
        directDownloadButton = new JButton("⏬ 直接下载");
        downloadButton = new JButton("⬇ 下载选中");
        selectAllButton = new JButton("✓ 全选");
        clearAllButton = new JButton("✗ 清空");

        tableModel = new MusicTableModel();
        resultTable = new JTable(tableModel);
        setupTable();
    }

    private void setupTable() {
        resultTable.setRowHeight(Constants.ROW_HEIGHT);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 设置列宽
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(Constants.COLUMN_WIDTH_SELECT);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(Constants.COLUMN_WIDTH_NAME);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(Constants.COLUMN_WIDTH_URL);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(Constants.COLUMN_WIDTH_STATUS);
    }

    // 布局方法组
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Constants.BACKGROUND_COLOR);

        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(Constants.BACKGROUND_COLOR);

        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel statusPanel = createStatusPanel();
        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        searchPanel.setBackground(Constants.PANEL_BG);

        JPanel inputContainerPanel = createInputContainerPanel();
        JPanel buttonPanel = createButtonPanel();

        searchPanel.add(inputContainerPanel, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel createInputContainerPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Constants.PANEL_BG);

        JPanel searchInputPanel = createSearchInputPanel();
        JPanel directDownloadPanel = createDirectDownloadPanel();

        container.add(searchInputPanel, BorderLayout.NORTH);
        container.add(directDownloadPanel, BorderLayout.CENTER);

        return container;
    }

    private JPanel createSearchInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Constants.PANEL_BG);

        JLabel searchLabel = new JLabel("搜索歌曲:");
        panel.add(searchLabel, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createDirectDownloadPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Constants.PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel directUrlLabel = new JLabel("直接下载:");
        panel.add(directUrlLabel, BorderLayout.WEST);
        panel.add(directUrlField, BorderLayout.CENTER);
        panel.add(directDownloadButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(Constants.PANEL_BG);

        panel.add(downloadButton);
        panel.add(selectAllButton);
        panel.add(clearAllButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 15, 15, 15));
        panel.setBackground(Constants.PANEL_BG);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        panel.setBackground(Constants.PANEL_BG);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        statusLabel = new JLabel("就绪");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    // 样式方法组
    private void applyStyles() {
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Color.BLACK);

        styleTextField(searchField);
        styleTextField(directUrlField);

        styleButton(searchButton, Constants.PRIMARY_COLOR);
        styleButton(downloadButton, Constants.SUCCESS_COLOR);
        styleButton(selectAllButton, Constants.SECONDARY_COLOR);
        styleButton(clearAllButton, Constants.ERROR_COLOR);
        styleButton(directDownloadButton, Constants.INFO_COLOR);

        styleTable();
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(Constants.LABEL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(Constants.BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addButtonHoverEffect(button, bgColor);
    }

    private void addButtonHoverEffect(JButton button, Color bgColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
        });
    }

    private void styleTable() {
        resultTable.setFont(Constants.TABLE_FONT);
        resultTable.setGridColor(new Color(240, 240, 240));
        resultTable.setShowGrid(true);

        resultTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        resultTable.getTableHeader().setBackground(new Color(250, 250, 250));
        resultTable.getTableHeader().setForeground(new Color(80, 80, 80));
        resultTable.getTableHeader().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Constants.PRIMARY_COLOR),
                new EmptyBorder(5, 5, 5, 5)
        ));

        resultTable.setSelectionBackground(new Color(220, 240, 255));
        resultTable.setSelectionForeground(Color.BLACK);
    }

    // 监听器方法组
    private void setupListeners() {
        searchButton.addActionListener(e -> searchMusic());
        directDownloadButton.addActionListener(e -> directDownload());
        downloadButton.addActionListener(e -> downloadSelected());
        selectAllButton.addActionListener(e -> selectAll());
        clearAllButton.addActionListener(e -> clearAll());

        setupTableListeners();
    }

    private void setupTableListeners() {
        // 复选框点击事件
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultTable.rowAtPoint(e.getPoint());
                int col = resultTable.columnAtPoint(e.getPoint());

                if (col == 0 && row >= 0 && row < musicList.size()) {
                    handleCheckboxClick(row);
                }
            }
        });

        // 双击查看详情
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = resultTable.getSelectedRow();
                    if (row >= 0 && row < musicList.size()) {
                        showMusicDetail(musicList.get(row));
                    }
                }
            }
        });
    }

    private void handleCheckboxClick(int row) {
        HifiniMusic music = musicList.get(row);
        boolean isSelected = (Boolean) tableModel.getValueAt(row, 0);

        if (isSelected) {
            selectedMusics.add(music);
        } else {
            selectedMusics.remove(music);
        }

        updateStatus();
    }

    // 窗口设置方法
    private void setupWindow() {
        setTitle("HiFi音乐下载器");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 搜索音乐方法
    private void searchMusic() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            showWarningDialog("请输入搜索关键词");
            return;
        }

        clearSearchResults();
        updateStatus("正在搜索: " + keyword);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                performSearch(keyword);
                return null;
            }

            @Override
            protected void done() {
                handleSearchCompletion();
            }
        };

        worker.execute();
    }

    private void clearSearchResults() {
        tableModel.setRowCount(0);
        musicList.clear();
        selectedMusics.clear();
    }

    private void performSearch(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            encodedKeyword = encodedKeyword.replace("%", "_");
            String searchUrl = Constants.SEARCH_URL_PREFIX + encodedKeyword + ".htm";

            String pageContent = NetworkUtils.downloadWebPage(searchUrl);
            if (pageContent.isEmpty()) {
                updateStatus("搜索失败: 未找到歌曲");
                return;
            }

            extractAndProcessMusicLinks(pageContent, keyword);

        } catch (Exception e) {
            updateStatus("搜索失败: " + e.getMessage());
        }
    }

    private void extractAndProcessMusicLinks(String pageContent, String keyword) {
        List<String> links = StringUtils.extractBetweenChars(pageContent, "<a", "/a>");
        int count = 0;

        for (String link : links) {
            if (link.contains(keyword)) {
                List<String> urlParts = StringUtils.extractBetweenChars(link, "\"", "\"");
                if (!urlParts.isEmpty() && urlParts.size() > 1) {
                    processMusicLink(urlParts.get(1).replace("\"", ""));
                    count++;

                    if (count >= 5) { // 限制搜索结果数量
                        break;
                    }
                }
            }
        }
    }

    private void processMusicLink(String link) {
        try {
            if (link.contains("htm") && !link.contains("http")) {
                link = Constants.BASE_URL + link;
            }

            String pageContent = NetworkUtils.downloadWebPage(link);
            HifiniMusic music = MusicParser.parseMusicInfo(pageContent);

            if (music != null && isValidMusicFormat(music.getDownUrl())) {
                musicList.add(music);
                tableModel.addMusic(music);
                updateStatus("找到一首: " + music.getName());
                Thread.sleep(2000); // 防止请求过快
            }
        } catch (Exception e) {
            System.err.println("处理音乐链接失败: " + e.getMessage());
        }
    }

    private boolean isValidMusicFormat(String url) {
        return url.contains(Constants.FLAC) ||
                url.contains(Constants.MP3) ||
                url.contains(Constants.M4A);
    }

    private void handleSearchCompletion() {
        if (musicList.isEmpty()) {
            updateStatus("未找到相关歌曲");
            showInfoDialog("未找到相关歌曲");
        } else {
            updateStatus("找到 " + musicList.size() + " 首歌曲");
        }
    }

    // 直接下载方法
    private void directDownload() {
        String url = directUrlField.getText().trim();
        if (url.isEmpty()) {
            showWarningDialog("请输入音乐文件URL");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            showErrorDialog("URL格式不正确，请以http://或https://开头");
            return;
        }

        if (createDownloadDirectoryFalse()) {
            return;
        }

        executeDirectDownload(url);
    }

    private void executeDirectDownload(String url) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private HifiniMusic music;
            private String fileName;

            @Override
            protected Boolean doInBackground() {
                try {
                    music = saveUrlMusicInfo(url);
                    if (music == null) {
                        return false;
                    }

                    fileName = generateFileName(music);
                    music.setSavePath(Constants.DOWNLOAD_PATH + fileName);

                    return NetworkUtils.downloadFile(music, music.getSavePath());
                } catch (Exception e) {
                    System.err.println("直接下载失败: " + e.getMessage());
                    return false;
                }
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                try {
                    boolean success = get();
                    handleDirectDownloadResult(success, music, fileName);
                } catch (Exception e) {
                    updateStatus("下载失败");
                    showErrorDialog("下载过程中发生错误: " + e.getMessage());
                }
            }
        };

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        updateStatus("正在下载...");
        worker.execute();
    }

    private HifiniMusic saveUrlMusicInfo(String url) {
        try {
            String pageContent = NetworkUtils.downloadWebPage(url);
            return MusicParser.parseMusicInfo(pageContent);
        } catch (Exception e) {
            System.err.println("获取音乐信息失败: " + e.getMessage());
            return null;
        }
    }

    private String generateFileName(HifiniMusic music) {
        String url = music.getDownUrl();
        String extension = url.substring(url.lastIndexOf("."));
        return music.getName() + extension;
    }

    private void handleDirectDownloadResult(boolean success, HifiniMusic music, String fileName) {
        if (success) {
            updateStatus("下载完成: " + fileName);
            musicList.add(music);
            tableModel.addMusic(music);
            showSuccessDialog("下载完成!\n文件保存位置: " + music.getSavePath());
        } else {
            updateStatus("下载失败");
            showErrorDialog("下载失败，请检查URL是否正确或网络连接");
        }
    }

    // 下载选中音乐方法
    private void downloadSelected() {
        if (selectedMusics.isEmpty()) {
            showWarningDialog("请先选择要下载的歌曲");
            return;
        }

        if (createDownloadDirectoryFalse()) {
            return;
        }

        startBatchDownload();
    }

    private boolean createDownloadDirectoryFalse() {
        File saveDir = new File(Constants.DOWNLOAD_PATH);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                showErrorDialog("无法创建保存目录: " + Constants.DOWNLOAD_PATH);
                return true;
            }
        }
        return false;
    }

    private void startBatchDownload() {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() {
                updateStatus("开始下载 " + selectedMusics.size() + " 首歌曲");

                for (HifiniMusic music : selectedMusics) {
                    executorService.submit(() -> {
                        boolean success = downloadSingleMusic(music);
                        synchronized (this) {
                            if (success) completed++;
                            else failed++;
                            publish(completed + failed);
                        }
                    });
                }

                waitForAllDownloads();
                return null;
            }

            private void waitForAllDownloads() {
                while ((completed + failed) < selectedMusics.size()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            @Override
            protected void process(List<Integer> chunks) {
                int processed = chunks.get(chunks.size() - 1);
                progressBar.setValue(processed);
                updateProgressStatus(processed);
                updateTableStatuses();
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                updateStatus("下载完成: 成功 " + completed + " 首, 失败 " + failed + " 首");
                showDownloadCompletionDialog(completed, failed);
            }
        };

        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setMaximum(selectedMusics.size());
        worker.execute();
    }

    private void updateProgressStatus(int processed) {
        updateStatus("下载中: " + processed + "/" + selectedMusics.size() +
                " (成功:" + completed + " 失败:" + failed + ")");
    }

    private void updateTableStatuses() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            HifiniMusic music = musicList.get(i);
            if (selectedMusics.contains(music)) {
                if (music.getSavePath() == null) {
                    continue;
                }
                File file = new File(music.getSavePath());
                if (file.exists()) {
                    tableModel.updateMusicStatus(i, "已下载");
                }
            }
        }
    }

    private int completed = 0;
    private int failed = 0;

    private boolean downloadSingleMusic(HifiniMusic music) {
        try {
            updateTableStatus(music, "下载中");

            String savePath = Constants.DOWNLOAD_PATH + generateFileName(music);
            music.setSavePath(savePath);

            boolean success = NetworkUtils.downloadFile(music, savePath);

            updateTableStatus(music, success ? "已下载" : "下载失败");
            return success;

        } catch (Exception e) {
            updateTableStatus(music, "下载失败");
            System.err.println("下载失败: " + music.getName() + " - " + e.getMessage());
            return false;
        }
    }

    private void updateTableStatus(HifiniMusic music, String status) {
        SwingUtilities.invokeLater(() -> {
            int index = musicList.indexOf(music);
            if (index >= 0) {
                tableModel.updateMusicStatus(index, status);
            }
        });
    }

    // 显示音乐详情方法
    private void showMusicDetail(HifiniMusic music) {
        JDialog dialog = new JDialog(this, "歌曲详情", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = createDetailPanel(music);
        JPanel buttonPanel = createDetailButtonPanel(dialog);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createDetailPanel(HifiniMusic music) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addDetailRow(panel, gbc, 0, "歌曲名称:", music.getName());
        addDetailRow(panel, gbc, 1, "下载地址:", music.getDownUrl());
        addDetailRow(panel, gbc, 2, "保存路径:", music.getSavePath());

        return panel;
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField field = new JTextField(value);
        field.setEditable(false);
        panel.add(field, gbc);
    }

    private JPanel createDetailButtonPanel(JDialog dialog) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("关闭");
        styleButton(closeButton, Constants.SECONDARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton);
        return panel;
    }

    // 辅助方法组
    private void selectAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 0);
            if (i < musicList.size()) {
                selectedMusics.add(musicList.get(i));
            }
        }
        updateStatus();
    }

    private void clearAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0);
        }
        selectedMusics.clear();
        updateStatus();
    }

    private void updateStatus() {
        updateStatus("已选择 " + selectedMusics.size() + " 首歌曲");
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    // 对话框方法组
    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.WARNING_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "下载成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDownloadCompletionDialog(int completed, int failed) {
        JDialog dialog = new JDialog(this, "下载完成", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
        iconLabel.setForeground(Constants.SUCCESS_COLOR);
        panel.add(iconLabel, gbc);

        gbc.gridy = 1;
        JLabel textLabel = new JLabel("<html><center>下载完成!<br>成功: " + completed + " 首<br>失败: " + failed + " 首</center></html>");
        textLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("确定");
        styleButton(okButton, Constants.PRIMARY_COLOR);
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // 主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                setupGlobalUIFonts();
                new MusicDownloader();
            } catch (Exception e) {
                System.err.println("设置外观失败: " + e.getMessage());
            }
        });
    }

    private static void setupGlobalUIFonts() {
        UIManager.put("Button.font", Constants.BUTTON_FONT);
        UIManager.put("Label.font", Constants.LABEL_FONT);
        UIManager.put("TextField.font", Constants.LABEL_FONT);
        UIManager.put("Table.font", Constants.TABLE_FONT);
    }
}