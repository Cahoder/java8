package orm.core;

import orm.bean.ColumnInfo;
import orm.bean.TableInfo;
import orm.utils.JavaFileUtils;
import orm.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取数据库中所有的数据表信息持久化到po包中去
 * Object Relation Mapping
 * Load and save all table structure information in the database to local memory
 */
public class TableContext {

    /**
     * 表名为key,表对象为value
     */
    public static Map<String, TableInfo> tables = new HashMap<>();

    /**
     * 将po包下的class和表信息对象关联起来,便于重用
     */
    public static Map<Class<?>, TableInfo> poClassTableMap = new HashMap<>();

    /*
     * 类加载时执行
     */
    static {
        Connection conn = DBManager.getConnection();
        try {
            DatabaseMetaData dbmd = conn.getMetaData();//获取数据库所有表的元信息
            ResultSet tablesRst = dbmd.getTables(null,"%","%",new String[]{"TABLE"});
            //循环所有表
            while (tablesRst.next()){
                String tName = tablesRst.getString("TABLE_NAME");
                TableInfo ti = new TableInfo(tName,new HashMap<>(),new ArrayList<>());
                tables.put(tName,ti);

                //循环表中的字段
                ResultSet fieldRst = dbmd.getColumns(null,"%",tName,"%");
                while (fieldRst.next()){
                    ColumnInfo ci = new ColumnInfo(
                            fieldRst.getString("COLUMN_NAME"),fieldRst.getString("TYPE_NAME"),0);
                    ti.getColumns().put(fieldRst.getString("COLUMN_NAME"),ci);
                }

                //循环表中的主键
                ResultSet priFieldRst = dbmd.getPrimaryKeys(null, "%", tName);
                while (priFieldRst.next()){
                    ColumnInfo pri = ti.getColumns().get(priFieldRst.getString("COLUMN_NAME"));
                    pri.setIsPrimaryField(1); //修改为主键
                    ti.getPrimaryKeys().add(pri); //加入联合主键中去
                }
                //取联合主键第一个为唯一主键
                if (ti.getPrimaryKeys().size()>0) ti.setPrimaryKey(ti.getPrimaryKeys().get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
         * 默认每次类加载更新一个数据库表与类结构信息
         */
        updateJavaPOFile();

        /*
         * 默认每次类加载更新po包下的class和表信息对象关联
         */
        loadJavaPOFileClazz();
    }

    /**
     * 更新数据表结构和Java持久化Clazz类对应信息
     */
    public static void updateJavaPOFile(){

        TypeConvert convertor = null;
        try {

            Class<?> convertClazz = Class.forName("orm.core.TypeConvert"
                    + StringUtils.firstCharToUpperCase(DBManager.getConfigs().getUsingDB()));

            convertor = (TypeConvert) convertClazz.newInstance(); //根据配置信息对象中正在使用的数据库，创建相对应的数据库转换器
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        if (convertor == null) throw new IllegalAccessError("db.config 文件配置信息中数据库类型填写有误!");

        for (TableInfo table : tables.values()) {
            JavaFileUtils.createJavaTableClazzPOFile(table,convertor);
        }
    }

    /**
     * 将生成的PO文件的载入生成class并和TableInfo关联起来
     */
    public static void loadJavaPOFileClazz(){
        for (TableInfo table : tables.values()){
            //动态编译
            JavaFileUtils.compilerJavaFile(
                    DBManager.getConfigs().getSrcRoot() + "/"
                            + DBManager.getConfigs().getPoPackage().replaceAll("\\.","\\/") + "/"
                            + StringUtils.firstCharToUpperCase(table.getTbName()) + ".java",
                      DBManager.getConfigs().getProRoot()//输出路径应该与项目编译的输出目录保持一致
                    );
            //加载类并关联起来
            try {
                Class<?> clazz = Class.forName(
                        DBManager.getConfigs().getPoPackage()
                        + "." + StringUtils.firstCharToUpperCase(table.getTbName())
                );
                poClassTableMap.put(clazz,table); //进行关联
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private TableContext() {}
}
