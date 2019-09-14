package jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.String;

//mp_record数据表的字段信息描述
@Target(value = {ElementType.FIELD})   //仅可用于成员属性
@Retention(value = RetentionPolicy.RUNTIME)  //运行时保留
public @interface MpRecordFieldAnnotation {
    boolean AUTO_INCREMENT() default false; //是否自动增长
    String FIELD_NAME();    //字段名字
    String FIELD_TYPE();    //字段类型
    int FIELD_LENGTH();  //字段长度
    String FIELD_COMMENT() default "";  //字段描述
    boolean IS_DEFAULT() default false; //是否有默认值
    String FIELD_DEFAULT() default "";  //字段默认值
    boolean IS_NULL() default true; //是否可为空
}
