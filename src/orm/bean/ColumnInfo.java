package orm.bean;

/**
 * 封装数据表中的一个字段信息
 * 字段名称
 * 字段类型
 * 是否主键
 */
public class ColumnInfo {
    private String fieldName; //字段名
    private String fieldType; //字段类型
    private int isPrimaryField; //是否主键(0/否,1/是)

    public ColumnInfo(String fieldName, String fieldType, int isPrimaryField) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isPrimaryField = isPrimaryField;
    }

    public ColumnInfo() {}

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

        public int getIsPrimaryField() {
        return isPrimaryField;
    }

    public void setIsPrimaryField(int isPrimaryField) {
        this.isPrimaryField = isPrimaryField;
    }
}
