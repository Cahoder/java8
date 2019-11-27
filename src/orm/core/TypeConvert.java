package orm.core;

/**
 * Use to convert field type between java and databases
 */
public interface TypeConvert {
    /**
     *
     * @param DatabaseType
     * @return
     */
    public String DatabaseType2JavaType(String DatabaseType);

    /**
     *
     * @param JavaType
     * @return
     */
    public String JavaType2DatabaseType(String JavaType);
}
