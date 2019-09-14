package jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.String;

//mp_record数据表的表信息描述
@Target(value = {ElementType.TYPE})  //仅可用于类
@Retention(value = RetentionPolicy.RUNTIME) //运行时保留
public @interface MpRecordTableAnnotation {
    //定义表的名字
    String TABLE();
    String ENGINE() default "InnoDB";
    String CHARSET() default "utf8";
    String COLLATE() default "utf8_unicode_ci";
    String COMMENT() default "";
    String[] SUPPLIERS() default {};
}
