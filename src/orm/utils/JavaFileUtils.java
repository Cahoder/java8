package orm.utils;

import orm.bean.ColumnInfo;
import orm.bean.Configuration;
import orm.bean.JavaFieldGetSet;
import orm.bean.TableInfo;
import orm.core.DBManager;
import orm.core.TypeConvert;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 封装JAVA常用的文件操作,可以用于生成JAVA源码
 */
public class JavaFileUtils {

    /**
     * 经指定的java文件编译成.class文件并输出到指定路径
     * @param javaFileInputPath 设置源码java文件路径
     * @param classFileOutputPath 设置编译选项,保存class文件输出的文件夹路径
     * @return boolean
     */
    public static boolean compilerJavaFile(String javaFileInputPath,String classFileOutputPath) {
        Iterable<String> options = Arrays.asList("-d", classFileOutputPath);
        StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler()  //JavaCompiler是一个单例模式
                .getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(Collections.singletonList(new File(javaFileInputPath)));

        return ToolProvider.getSystemJavaCompiler().getTask(null, fileManager, null, options,
                null, compilationUnits).call();
    }

    /**
     * 创建数据表对应Java类的持久化源文件
     * @param table 数据表信息
     * @param convertor 数据库类型转换器
     */
    public static void createJavaTableClazzPOFile(TableInfo table, TypeConvert convertor){
        String tableClazzStr = createJavaTableClazzSRC(table,convertor); //创建类的源字符串

        String projectRoot = DBManager.getConfigs().getSrcRoot(); //获取项目根目录
        String poPackage = DBManager.getConfigs().getPoPackage().replaceAll("\\.","\\/"); //获取po包名称并转换为路径

        File file = new File(projectRoot + "/" + poPackage);
        if (!file.exists()) file.mkdir(); //不存在则创建文件夹

        //PO写入
        try(BufferedWriter bfw =
            new BufferedWriter(new FileWriter(file.getPath()+"/"
                    + StringUtils.firstCharToUpperCase(table.getTbName()) + ".java"))
        ) {
            bfw.write(tableClazzStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库字段对应Java类的成员属性信息，以及其get和set方法源码
     * @param field 字段信息
     * @param convertor 数据库数据类型转换器
     * @return java成员属性和get/set方法
     */
    public static JavaFieldGetSet createJavaFieldGetSetSRC(ColumnInfo field, TypeConvert convertor){
        JavaFieldGetSet jfgs = new JavaFieldGetSet();
        String javaFieldType = convertor.DatabaseType2JavaType(field.getFieldType());

        //生成成员属性源码
        jfgs.setFieldAttStr("\tprivate " + javaFieldType + " " + field.getFieldName() + ";\n");

        //生成get成员方法源码
        jfgs.setFieldGetMedStr("\tpublic " + javaFieldType + " get" + StringUtils.firstCharToUpperCase(field.getFieldName()) + "(){\n"
                                + "\t\treturn this." + field.getFieldName() + ";\n"
                                + "\t}\n");

        //生成set成员方法源码
        jfgs.setFieldSetMedStr("\tpublic void set" + StringUtils.firstCharToUpperCase(field.getFieldName())
                                + "("+ javaFieldType + " " + field.getFieldName() + "){\n"
                                + "\t\tthis." + field.getFieldName() + " = " + field.getFieldName() + ";\n"
                                + "\t}\n");

        return jfgs;
    }

    /**
     * 创建数据表对应Java类的源码
     * 包名字
     * import语句
     * 类名
     * 成员属性
     * 成员方法(get/set)
     * @param table 数据库表信息
     * @param convertor 数据库数据类型转换器
     * @return Java类源码字符串
     */
    public static String createJavaTableClazzSRC(TableInfo table, TypeConvert convertor){
        Map<String, ColumnInfo> fields = table.getColumns();
        List<JavaFieldGetSet> fieldsGetSet = new ArrayList<>();
        for (ColumnInfo field : fields.values()) {
            fieldsGetSet.add(createJavaFieldGetSetSRC(field,convertor));
        }

        StringBuilder clazzSrc = new StringBuilder();

        //包名字
        Configuration configs = DBManager.getConfigs(); //获取配置信息对象
        clazzSrc.append("package ").append(configs.getPoPackage()).append(";\n\n");
        //import 语句
        clazzSrc.append("import java.sql.*;\n");
        clazzSrc.append("import java.util.*;\n");
        clazzSrc.append("\n");
        //类名
        clazzSrc.append("public class ").append(StringUtils.firstCharToUpperCase(table.getTbName())).append(" {\n\n");
        //成员属性
        for (JavaFieldGetSet f : fieldsGetSet) {
            clazzSrc.append(f.getFieldAttStr());
        }
        clazzSrc.append("\n");
        //成员方法set
        for (JavaFieldGetSet f : fieldsGetSet) {
            clazzSrc.append(f.getFieldSetMedStr());
        }
        //成员方法get
        for (JavaFieldGetSet f : fieldsGetSet) {
            clazzSrc.append(f.getFieldGetMedStr());
        }
        //结束
        clazzSrc.append("}");

        return clazzSrc.toString();
    }
}
