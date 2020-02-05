package orm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装JAVA常用的反射类操作
 */
public class ReflectUtils {

    /**
     * 通过反射调用一个对象的getter方法获取数据表指定字段对应在Java对象中的成员属性
     * @param obj 数据表对应的Java对象
     * @param fieldName 字段名称
     * @return 返回该字段在Java对象中的成员属性
     */
    public static Object invokeGetter(Object obj,String fieldName){
        Object attribute = null;
        Class<?> clazz = obj.getClass();
        try {
            Method getter = clazz.getMethod("get"+StringUtils.firstCharToUpperCase(fieldName));   //只能获取public属性的方法
            //调用getter方法获取属性值
            attribute = getter.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return attribute;
    }

    /**
     * 通过反射调用一个对象的setter方法赋值数据表指定字段值对应在Java对象中的成员属性
     * @param obj 数据表对应的Java对象
     * @param fieldName 字段名称
     * @param fieldValue 字段值
     */
    public static void invokeSetter(Object obj,String fieldName,Object fieldValue){
        Class<?> clazz = obj.getClass();
        try {
            Method setter = clazz.getMethod( //只能获取public属性的方法
                    "set"+StringUtils.firstCharToUpperCase(fieldName),clazz.getDeclaredField(fieldName).getType());
            //调用setter方法设置属性值
            setter.invoke(obj,fieldValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
