package orm.utils;

/**
 * 封装JAVA常用的字符串拼接操作
 */
public class StringUtils {
    /**
     * 将给定的字符串首字母大写
     * @param str 元字符
     * @return 首字母大写后的字符
     */
    public static String firstCharToUpperCase(String str){
        return str.toUpperCase().substring(0,1)+str.substring(1);
    }
}
