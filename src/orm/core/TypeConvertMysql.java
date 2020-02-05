package orm.core;

/**
 * Mysql Database 数据类型转换
 */
public class TypeConvertMysql implements TypeConvert {

    @Override
    public String DatabaseType2JavaType(String DatabaseType) {
        String JavaType = null;

        if ("bit".equalsIgnoreCase(DatabaseType)
        ) JavaType = "Boolean";

        else if ("int".equalsIgnoreCase(DatabaseType)
                || "smallint".equalsIgnoreCase(DatabaseType)
                || "tinyint".equalsIgnoreCase(DatabaseType)
                || "integer".equalsIgnoreCase(DatabaseType)
        ) JavaType = "Integer";

        else if("bigint".equalsIgnoreCase(DatabaseType)
                || "int unsigned".equalsIgnoreCase(DatabaseType)
        ) JavaType = "Long";

        else if("double".equalsIgnoreCase(DatabaseType)
        ) JavaType = "Double";

        else if ("varchar".equalsIgnoreCase(DatabaseType)
                || "char".equalsIgnoreCase(DatabaseType)
                || "text".equalsIgnoreCase(DatabaseType)
                || "tinytext".equalsIgnoreCase(DatabaseType)
                || "mediumtext".equalsIgnoreCase(DatabaseType)
                || "longtext".equalsIgnoreCase(DatabaseType)
        ) JavaType = "String";

        else if ("blob".equalsIgnoreCase(DatabaseType)
                || "tinyblob".equalsIgnoreCase(DatabaseType)
                || "longblob".equalsIgnoreCase(DatabaseType)
                || "mediumblob".equalsIgnoreCase(DatabaseType)
        ) JavaType = "java.sql.Blob";

        else if ("date".equalsIgnoreCase(DatabaseType)
                || "year".equalsIgnoreCase(DatabaseType)
        ) JavaType = "java.sql.Date";

        else if ("time".equalsIgnoreCase(DatabaseType)
        ) JavaType = "java.sql.Time";

        else if ("timestamp".equalsIgnoreCase(DatabaseType)
                || "datetime".equalsIgnoreCase(DatabaseType)
        ) JavaType = "java.sql.Timestamp";

        else if ("decimal".equalsIgnoreCase(DatabaseType)
        ) JavaType = "java.math.BigDecimal";

        else {
            System.out.println("当前还未支持Mysql的" + DatabaseType + "数据类型!");
        }

        return JavaType;
    }

    @Override
    public String JavaType2DatabaseType(String JavaType) {
        return null;
    }
}
