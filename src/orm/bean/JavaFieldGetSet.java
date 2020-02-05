package orm.bean;

/**
 * 封装用于生成字段成员属性的Get和Set方法的JavaBean
 */
public class JavaFieldGetSet {
    /**
     * 字段相应成员属性源代码的字符串--> private 字段对应的Java数据类型 字段名称;
     */
    private String FieldAttStr;
    /**
     * 字段相应get成员方法源代码的字符串--> public 字段对应的Java数据类型 get字段名称(){return this.字段名称};
     */
    private String FieldGetMedStr;
    /**
     * 字段相应set成员方法源代码的字符串--> public 字段对应的Java数据类型 set字段名称(字段对应的Java数据类型 字段名称){this.字段名称 = 字段名称};
     */
    private String FieldSetMedStr;

    public JavaFieldGetSet() {}

    public JavaFieldGetSet(String fieldAttStr, String fieldGetMedStr, String fieldSetMedStr) {
        FieldAttStr = fieldAttStr;
        FieldGetMedStr = fieldGetMedStr;
        FieldSetMedStr = fieldSetMedStr;
    }

    public String getFieldAttStr() {
        return FieldAttStr;
    }

    public void setFieldAttStr(String fieldAttStr) {
        FieldAttStr = fieldAttStr;
    }

    public String getFieldGetMedStr() {
        return FieldGetMedStr;
    }

    public void setFieldGetMedStr(String fieldGetMedStr) {
        FieldGetMedStr = fieldGetMedStr;
    }

    public String getFieldSetMedStr() {
        return FieldSetMedStr;
    }

    public void setFieldSetMedStr(String fieldSetMedStr) {
        FieldSetMedStr = fieldSetMedStr;
    }

    @Override
    public String toString() {
        System.out.print(FieldAttStr);
        System.out.print(FieldGetMedStr);
        System.out.print(FieldSetMedStr);
        return super.toString();
    }
}
