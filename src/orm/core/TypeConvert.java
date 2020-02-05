package orm.core;

/**
 * Use to convert field type between java and databases
 */
public interface TypeConvert {
    /**
     *
     * @param DatabaseType 数据库数据类型
     * @return Java数据类型
     */
    String DatabaseType2JavaType(String DatabaseType);

    /**
     *
     * @param JavaType Java数据类型
     * @return 数据库数据类型
     */
    String JavaType2DatabaseType(String JavaType);
}