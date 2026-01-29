package music;

import javax.swing.table.DefaultTableModel;

public class MusicTableModel extends DefaultTableModel {
    
    public MusicTableModel() {
        super(Constants.TABLE_COLUMN_NAMES, 0);
    }
    
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
    
    /**
     * 添加音乐到表格
     */
    public void addMusic(HifiniMusic music) {
        Object[] rowData = {
            false,
            music.getName(),
            StringUtils.shortenUrl(music.getDownUrl()),
            "待下载"
        };
        addRow(rowData);
    }
    
    /**
     * 更新音乐状态
     */
    public void updateMusicStatus(int row, String status) {
        setValueAt(status, row, 3);
    }
}