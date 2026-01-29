package music;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 音乐搜索下载GUI界面
 */
public class MusicDownloaderGUI extends JFrame {
    public static final String BASE = "https://www.hifini.com.cn/";
    public static final String HEAD = "https://www.hifini.com.cn/search-";
    public static final String PATH = "D:/BaiduNetdiskDownload/music/";

    private static final int BUFFER_SIZE = 8192;
    // 颜色定义
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // 主色调 - 蓝色
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // 次要色调
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113); // 成功颜色
    private static final Color ERROR_COLOR = new Color(231, 76, 60); // 错误颜色
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // 背景色
    private static final Color PANEL_BG = new Color(255, 255, 255); // 面板背景色
    private static final Color INFO_COLOR = new Color(241, 196, 15); // 信息颜色（黄色）

    // 字体定义
    private static final Font TITLE_FONT = new Font("Microsoft YaHei", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
    private static final Font TABLE_FONT = new Font("Microsoft YaHei", Font.PLAIN, 13);

    private JTextField searchField;
    private JTextField directUrlField;
    private JButton searchButton;
    private JButton downloadButton;
    private JButton selectAllButton;
    private JButton clearAllButton;
    private JButton directDownloadButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JPanel contentPanel;
    private JScrollPane scrollPane;

    private final List<HifiniMusic> musicList = new ArrayList<>();
    private final Set<HifiniMusic> selectedMusics = new HashSet<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public MusicDownloaderGUI() {
        initComponents();
        setupLayout();
        setupListeners();
        applyStyles();
        setTitle("HiFi音乐下载器");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        // 标题标签
        titleLabel = new JLabel("HiFi音乐下载器");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 搜索框
        searchField = new JTextField();
        searchField.setToolTipText("请输入歌曲名称进行搜索");

        // 直接下载URL输入框
        directUrlField = new JTextField();
        directUrlField.setToolTipText("直接输入音乐文件URL进行下载");

        // 搜索按钮
        searchButton = new JButton("🔍 搜索");
        searchButton.setToolTipText("搜索音乐");

        // 直接下载按钮
        directDownloadButton = new JButton("⏬ 直接下载");
        directDownloadButton.setToolTipText("直接下载URL指定的音乐文件");

        // 操作按钮
        downloadButton = new JButton("⬇ 下载选中");
        downloadButton.setToolTipText("下载选中的歌曲");

        selectAllButton = new JButton("✓ 全选");
        selectAllButton.setToolTipText("全选所有歌曲");

        clearAllButton = new JButton("✗ 清空");
        clearAllButton.setToolTipText("清除所有选择");

        // 表格模型
        String[] columnNames = {"选择", "歌曲名称", "下载地址", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(35);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 设置列宽
        TableColumnModel columnModel = resultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setMaxWidth(80);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(3).setMaxWidth(120);

        scrollPane = new JScrollPane(resultTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 进度条
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        // 状态标签
        statusLabel = new JLabel("就绪");
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // 内容面板
        contentPanel = new JPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // 标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // 搜索面板
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)), new EmptyBorder(15, 15, 15, 15)));
        searchPanel.setBackground(PANEL_BG);

        JPanel searchInputPanel = new JPanel(new BorderLayout(5, 0));
        searchInputPanel.setBackground(PANEL_BG);

        JLabel searchLabel = new JLabel("搜索歌曲:");
        searchInputPanel.add(searchLabel, BorderLayout.WEST);
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        // 直接下载面板
        JPanel directDownloadPanel = new JPanel(new BorderLayout(5, 0));
        directDownloadPanel.setBackground(PANEL_BG);
        directDownloadPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // 顶部留白

        JLabel directUrlLabel = new JLabel("直接下载:");
        directDownloadPanel.add(directUrlLabel, BorderLayout.WEST);
        directDownloadPanel.add(directUrlField, BorderLayout.CENTER);
        directDownloadPanel.add(directDownloadButton, BorderLayout.EAST);

        // 将搜索和直接下载面板组合
        JPanel inputContainerPanel = new JPanel(new BorderLayout());
        inputContainerPanel.setBackground(PANEL_BG);
        inputContainerPanel.add(searchInputPanel, BorderLayout.NORTH);
        inputContainerPanel.add(directDownloadPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.add(downloadButton);
        buttonPanel.add(selectAllButton);
        buttonPanel.add(clearAllButton);

        searchPanel.add(inputContainerPanel, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(0, 15, 15, 15));
        tablePanel.setBackground(PANEL_BG);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 状态面板
        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)), new EmptyBorder(10, 15, 10, 15)));
        statusPanel.setBackground(PANEL_BG);
        statusPanel.add(progressBar, BorderLayout.CENTER);
        statusPanel.add(statusLabel, BorderLayout.EAST);

        // 主内容面板
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        // 添加到主窗口
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void applyStyles() {
        // 设置字体
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.BLACK);

        searchField.setFont(LABEL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true), new EmptyBorder(8, 10, 8, 10)));

        directUrlField.setFont(LABEL_FONT);
        directUrlField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true), new EmptyBorder(8, 10, 8, 10)));

        // 设置按钮样式
        styleButton(searchButton, PRIMARY_COLOR);
        styleButton(downloadButton, SUCCESS_COLOR);
        styleButton(selectAllButton, SECONDARY_COLOR);
        styleButton(clearAllButton, ERROR_COLOR);
        styleButton(directDownloadButton, INFO_COLOR);

        // 设置表格样式
        resultTable.setFont(TABLE_FONT);
        resultTable.setGridColor(new Color(240, 240, 240));
        resultTable.setShowGrid(true);
        resultTable.setRowHeight(35);
        resultTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        resultTable.getTableHeader().setBackground(new Color(250, 250, 250));
        resultTable.getTableHeader().setForeground(new Color(80, 80, 80));
        resultTable.getTableHeader().setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 2, 0, PRIMARY_COLOR), new EmptyBorder(5, 5, 5, 5)));
        resultTable.setSelectionBackground(new Color(220, 240, 255));
        resultTable.setSelectionForeground(Color.BLACK);

        // 设置滚动条样式
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);

        // 设置进度条样式
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBackground(new Color(240, 240, 240));

        // 设置状态标签样式
        statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(120, 120, 120));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        // 修改这里：默认状态用黑色文字
        button.setForeground(Color.BLACK);  // 改为黑色文字
        button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(bgColor.darker(), 1), new EmptyBorder(8, 20, 8, 20)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(bgColor.darker().brighter(), 1), new EmptyBorder(8, 20, 8, 20)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(bgColor.darker(), 1), new EmptyBorder(8, 20, 8, 20)));
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

    private void setupListeners() {
        // 搜索按钮事件
        searchButton.addActionListener(e -> searchMusic());
        // 直接下载按钮事件
        directDownloadButton.addActionListener(e -> directDownload());
        // 下载按钮事件
        downloadButton.addActionListener(e -> downloadSelected());
        // 全选按钮
        selectAllButton.addActionListener(e -> selectAll());
        // 清空按钮
        clearAllButton.addActionListener(e -> clearAll());
        // 表格复选框事件
        resultTable.addMouseListener(mouseListener);
        // 双击行查看详情
        resultTable.addMouseListener(mouseAdapter);
    }

    /**
     * 直接下载音乐文件
     */
    private void directDownload() {
        String url = directUrlField.getText().trim();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入音乐文件URL", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 检查URL格式
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            JOptionPane.showMessageDialog(this, "URL格式不正确，请以http://或https://开头", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 检查保存目录
        File saveDir = new File(PATH);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "无法创建保存目录: " + PATH, "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        HifiniMusic music = saveUrlMusicInfo(url);

        String fileName = music.getName() + music.downUrl.substring(music.downUrl.lastIndexOf("."));
        music.setSavePath(PATH + fileName);

        // 显示进度条
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        updateStatus("正在下载: " + fileName);

        // 在新线程中执行下载
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    return downloadFile(music);
                } catch (Exception e) {
                    System.out.println("直接下载失败: " + e.getMessage());
                    return false;
                }
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                try {
                    boolean success = get();
                    if (success) {
                        updateStatus("下载完成: " + fileName);
                        JOptionPane.showMessageDialog(MusicDownloaderGUI.this, "下载完成!\n文件保存位置: " + music.getSavePath(), "下载成功", JOptionPane.INFORMATION_MESSAGE);

                        // 添加到表格中显示
                        musicList.add(music);
                        tableModel.addRow(new Object[]{false, music.getName(), shortenUrl(music.getDownUrl()), "已下载"});
                    } else {
                        updateStatus("下载失败");
                        JOptionPane.showMessageDialog(MusicDownloaderGUI.this, "下载失败，请检查URL是否正确或网络连接", "下载失败", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    updateStatus("下载失败");
                    JOptionPane.showMessageDialog(MusicDownloaderGUI.this, "下载过程中发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = resultTable.rowAtPoint(e.getPoint());
            int col = resultTable.columnAtPoint(e.getPoint());

            if (col == 0 && row >= 0 && row < musicList.size()) {
                HifiniMusic music = musicList.get(row);
                boolean isSelected = (Boolean) tableModel.getValueAt(row, 0);

                if (isSelected) {
                    selectedMusics.add(music);
                } else {
                    selectedMusics.remove(music);
                }

                updateStatus();
            }
        }
    };

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int row = resultTable.getSelectedRow();
                if (row >= 0 && row < musicList.size()) {
                    HifiniMusic music = musicList.get(row);
                    showMusicDetail(music);
                }
            }
        }
    };

    /**
     * 显示音乐详情
     */
    private void showMusicDetail(HifiniMusic music) {
        JDialog dialog = new JDialog(this, "歌曲详情", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("歌曲名称:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField nameField = new JTextField(music.getName());
        nameField.setEditable(false);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("下载地址:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField urlField = new JTextField(music.getDownUrl());
        urlField.setEditable(false);
        panel.add(urlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("保存路径:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField pathField = new JTextField(music.getSavePath());
        pathField.setEditable(false);
        panel.add(pathField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("关闭");
        styleButton(closeButton, SECONDARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * 搜索音乐
     */
    private void searchMusic() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入搜索关键词", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 清空旧数据
        clearTable();
        musicList.clear();
        selectedMusics.clear();
        // 在新线程中执行搜索
        SwingWorker<Void, HifiniMusic> worker = new SwingWorker<Void, HifiniMusic>() {
            @Override
            protected Void doInBackground() {
                updateStatus("正在搜索: " + keyword);
                String curr;
                try {
                    curr = java.net.URLEncoder.encode(keyword, "UTF-8");
                    curr = curr.replace("%", "_");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String downLoadUrl = HEAD + curr + ".htm";
                String page = downPage(downLoadUrl);
                if (page.isEmpty()) {
                    updateStatus("搜索: " + keyword + " 失败,未找到歌曲");
                    return null;
                }
                List<String> matcher = extractSubstringBetweenChars(page, "<a", "/a>");
                int count = 0;
                for (String value : matcher) {
                    if (value.contains(keyword)) {
                        List<String> result = extractSubstringBetweenChars(value, "\"", "\"");
                        if (!result.isEmpty()) {
                            String name = extractChinese(value);
                            downLoadUrl = result.get(1).replace("\"", "");
                            if (downLoadUrl.contains("htm")) {
                                if (!downLoadUrl.contains("http")) {
                                    downLoadUrl = BASE + downLoadUrl;
                                }
                                downLoadUrl = BASE + downLoadUrl;
                                HifiniMusic music = new HifiniMusic(name, downLoadUrl, PATH + name + downLoadUrl.substring(downLoadUrl.indexOf(".")));
                                try {// 获取真实下载地址
                                    saveMusicInfo(music);
                                    publish(music);
                                    count++;
                                    if (count >= 50) { // 限制搜索结果数量
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("获取音乐信息失败: " + e.getMessage());
                                }
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            protected void process(List<HifiniMusic> chunks) {
                for (HifiniMusic music : chunks) {
                    musicList.add(music);
                    tableModel.addRow(new Object[]{false, music.getName(), shortenUrl(music.getDownUrl()), "待下载"});
                }
            }

            @Override
            protected void done() {
                if (musicList.isEmpty()) {
                    updateStatus("未找到相关歌曲");
                    JOptionPane.showMessageDialog(MusicDownloaderGUI.this, "未找到相关歌曲", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateStatus("找到 " + musicList.size() + " 首歌曲");
                }
            }
        };
        worker.execute();
    }

    private void saveMusicInfo(HifiniMusic music) {
        String page = downPage(music.getDownUrl());
        if (page.contains("music") && page.contains("url") && page.contains("pic")) {
            page = page.trim().substring(page.indexOf("music"));
            try {
                String result = page;
                int time = 10;
                while (result.contains("url") && result.contains("pic") && --time >= 0) {
                    result = result.substring(result.indexOf("url") + 6, result.indexOf("pic") + 3);
                }
                result = result.substring(result.indexOf("https"), result.indexOf(",") - 1);
                music.setDownUrl(result);
            } catch (Exception e) {
                System.out.println("saveMusicInfo 报错: " + e);
            }
        }
    }

    private HifiniMusic saveUrlMusicInfo(String url) {
        HifiniMusic music = null;
        String page = downPage(url);
        if (page.contains("music") && page.contains("title") && page.contains("url") && page.contains("pic") && page.contains("author")) {
            String result = page.trim().substring(page.indexOf("music"));
            music = new HifiniMusic();
            try {
                music.setName(result.substring(result.indexOf("title") + 8, result.indexOf("author") - 1));
                music.setName(music.getName().trim());
                music.setName(music.getName().replace("\"", ""));
                music.setName(music.getName().replace(",", ""));
                result = result.substring(result.indexOf("url") + 6, result.indexOf("pic") + 3);
                result = result.substring(result.indexOf("https"), result.indexOf(",") - 1);
                music.setDownUrl(result);
            } catch (Exception e) {
                System.out.println("saveMusicInfo 报错: " + e);
            }
        }
        return music;
    }

    /**
     * 下载选中的音乐
     */
    private void downloadSelected() {
        if (selectedMusics.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先选择要下载的歌曲", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 检查保存目录
        File saveDir = new File(PATH);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "无法创建保存目录: " + PATH, "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 显示进度条
        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setMaximum(selectedMusics.size());

        // 在新线程中执行下载
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            private int completed = 0;
            private int failed = 0;

            @Override
            protected Void doInBackground() {
                updateStatus("开始下载 " + selectedMusics.size() + " 首歌曲");

                for (HifiniMusic music : selectedMusics) {
                    try {
                        executorService.submit(() -> {
                            boolean success = downloadSingleMusic(music);
                            if (success) {
                                completed++;
                            } else {
                                failed++;
                            }
                            publish(completed + failed);
                        });
                    } catch (Exception e) {
                        failed++;
                        publish(completed + failed);
                    }
                }

                // 等待所有任务完成
                while ((completed + failed) < selectedMusics.size()) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.out.println("sleep error " + e);
                    }
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int processed = chunks.get(chunks.size() - 1);
                progressBar.setValue(processed);
                updateStatus("下载中: " + processed + "/" + selectedMusics.size() + " (成功:" + completed + " 失败:" + failed + ")");

                // 更新表格状态
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    HifiniMusic music = musicList.get(i);
                    if (selectedMusics.contains(music)) {
                        // 检查文件是否已下载
                        File file = new File(music.getSavePath());
                        if (file.exists()) {
                            tableModel.setValueAt("已下载", i, 3);
                        }
                    }
                }
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                updateStatus("下载完成: 成功 " + completed + " 首, 失败 " + failed + " 首");

                // 显示完成对话框
                JDialog dialog = new JDialog(MusicDownloaderGUI.this, "下载完成", true);
                dialog.setLayout(new BorderLayout());
                dialog.setSize(300, 200);
                dialog.setLocationRelativeTo(MusicDownloaderGUI.this);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(new EmptyBorder(20, 20, 20, 20));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(10, 10, 10, 10);

                // 添加图标和文本
                JLabel iconLabel = new JLabel("✓");
                iconLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
                iconLabel.setForeground(SUCCESS_COLOR);
                panel.add(iconLabel, gbc);

                gbc.gridy = 1;
                JLabel textLabel = new JLabel("下载完成!成功: " + completed + " 首失败: " + failed + " 首");
                textLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                textLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(textLabel, gbc);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton okButton = new JButton("确定");
                styleButton(okButton, PRIMARY_COLOR);
                okButton.addActionListener(e -> dialog.dispose());
                buttonPanel.add(okButton);

                dialog.add(panel, BorderLayout.CENTER);
                dialog.add(buttonPanel, BorderLayout.SOUTH);

                dialog.setVisible(true);
            }
        };

        worker.execute();
    }

    private boolean downloadSingleMusic(HifiniMusic music) {
        try {
            updateTableStatus(music, "下载中");
            boolean success = downloadFile(music);
            updateTableStatus(music, success ? "已下载" : "下载失败");
            return success;
        } catch (Exception e) {
            updateTableStatus(music, "下载失败");
            System.out.println("下载失败: " + music.getName() + " - " + e);
            return false;
        }
    }

    private void updateTableStatus(HifiniMusic music, String status) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (musicList.get(i).equals(music)) {
                    tableModel.setValueAt(status, i, 3);
                    // 根据状态设置文本颜色
                    if ("已下载".equals(status)) {
                        resultTable.setValueAt("<html><font color='#2ECC71'>" + status + "</font></html>", i, 3);
                    } else if ("下载失败".equals(status)) {
                        resultTable.setValueAt("<html><font color='#E74C3C'>" + status + "</font></html>", i, 3);
                    } else if ("下载中".equals(status)) {
                        resultTable.setValueAt("<html><font color='#3498DB'>" + status + "</font></html>", i, 3);
                    }
                    break;
                }
            }
        });
    }

    /**
     * 全选
     */
    private void selectAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 0);
            if (i < musicList.size()) {
                selectedMusics.add(musicList.get(i));
            }
        }
        updateStatus();
    }

    /**
     * 清空选择
     */
    private void clearAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0);
        }
        selectedMusics.clear();
        updateStatus();
    }

    /**
     * 清空表格
     */
    private void clearTable() {
        tableModel.setRowCount(0);
    }

    /**
     * 更新状态
     */
    private void updateStatus() {
        updateStatus("已选择 " + selectedMusics.size() + " 首歌曲");
    }

    /**
     * 更新状态
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * 缩短URL显示
     */
    private String shortenUrl(String url) {
        if (url.length() > 50) {
            return url.substring(0, 30) + "..." + url.substring(url.length() - 20);
        }
        return url;
    }

    /**
     * 提取从startChar到endChar之间的内容
     *
     * @param input     总内容
     * @param startChar 字符开头
     * @param endChar   字符结尾
     */
    public List<String> extractSubstringBetweenChars(String input, String startChar, String endChar) {
        List<String> array = new ArrayList<>();
        // String regex = "[\\u4e00-\\u9fa5]+";//匹配中文
        String regex = Pattern.quote(startChar) + "(.*?)" + Pattern.quote(endChar);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            array.add(matcher.group());
        }
        return array;
    }

    /**
     * 提取中文
     *
     * @param input 总内容
     */
    public String extractChinese(String input) {
        StringBuilder sb = new StringBuilder();
        String regex = "[\\u4e00-\\u9fa5]+";// 匹配中文
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    /**
     * 下载文件
     */
    public String downPage(String url) {
        String result = "";
        try {
            result = downloadWebPage(url).trim();
        } catch (Exception e) {
            System.out.println("downPage 报错" + e);
        }
        return result;
    }

    /**
     * 从地址 urlString 下载文件保存成String
     *
     * @param urlString 下载地址
     */
    public String downloadWebPage(String urlString) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        URL url = new URL(urlString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }
        return contentBuilder.toString();
    }

    public static void main(String[] args) {
        // 设置外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // 设置全局UI属性
            UIManager.put("Button.font", BUTTON_FONT);
            UIManager.put("Label.font", LABEL_FONT);
            UIManager.put("TextField.font", LABEL_FONT);
            UIManager.put("Table.font", TABLE_FONT);

        } catch (Exception e) {
            System.out.println("setLookAndFeel " + e);
        }

        // 启动界面
        SwingUtilities.invokeLater(() -> {
            MusicDownloaderGUI gui = new MusicDownloaderGUI();
            gui.setVisible(true);
        });
    }

    public boolean downloadFile(HifiniMusic music) {
        try {
            long start = System.currentTimeMillis();
            FileOutputStream fos;
            BufferedInputStream bis;
            HttpURLConnection httpURLConnection;
            URL url;
            byte[] buf = new byte[BUFFER_SIZE];
            int size;
            url = new URL(music.downUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            fos = new FileOutputStream(music.savePath);
            System.out.println("正在获取链接[" + music.downUrl + "]的内容");
            System.out.println("将其保存为文件[" + music.savePath + "]");
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            httpURLConnection.disconnect();

            long fileSizeInBytes = getFileSize(music.savePath);
            double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);

            System.out.println("文件：" + music.name);
            System.out.println("大小:" + String.format("%.1f", fileSizeInMB) + " mb");
            System.out.println("消耗: " + (System.currentTimeMillis() - start) + " ms");
            System.out.println("下载完成，保存为 " + music.savePath);
            return true;
        } catch (Exception e) {
            System.out.println("文件下载失败，信息：" + e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件大小
     */
    private long getFileSize(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File does not exist: " + filePath);
        }

        if (!file.isFile()) {
            throw new Exception("Not a regular file: " + filePath);
        }

        return file.length();
    }

    public static class HifiniMusic {
        private String name;

        private String downUrl;

        private String savePath;

        public HifiniMusic(String name, String downUrl, String savePath) {
            this.name = name;
            this.downUrl = downUrl;
            this.savePath = savePath;
        }

        public HifiniMusic() {
        }

        public String getName() {
            return name;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        @Override
        public String toString() {
            return "HifiniMusic{" + "name='" + name + '\'' + ", downUrl='" + downUrl + '\'' + ", savePath='" + savePath + '\'' + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HifiniMusic music = (HifiniMusic) o;
            return Objects.equals(name, music.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}