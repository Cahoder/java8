package orm.bean;

import java.util.List;
import java.util.Map;

/**
 * 封装数据库中的表结构信息
 */
public class TableInfo {
    private String tbName; //表名
    private Map<String, ColumnInfo> columns; //所有字段信息
    /*以下两个成员属性只能选其一*/
    private ColumnInfo primaryKey; //唯一主键
    private List<ColumnInfo> primaryKeys; //联合主键


    public TableInfo() {}

    /**
     * @param tbName 数据表名
     * @param columns 表中的所有字段
     * @param primaryKey 表中的唯一主键
     */
    public TableInfo(String tbName, Map<String, ColumnInfo> columns, ColumnInfo primaryKey) {
        this.tbName = tbName;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    /**
     * @param tbName 数据表名
     * @param columns 表中的所有字段
     * @param primaryKeys 表中的所有联合主键
     */
    public TableInfo(String tbName, Map<String, ColumnInfo> columns, List<ColumnInfo> primaryKeys) {
        this.tbName = tbName;
        this.columns = columns;
        this.primaryKeys = primaryKeys;
    }

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }

    public ColumnInfo getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnInfo primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnInfo> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<ColumnInfo> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}