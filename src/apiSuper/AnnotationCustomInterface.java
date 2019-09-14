package apiSuper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.String;

/**
 * 自定义注解接口
 */
@Target(value = {ElementType.METHOD,ElementType.CONSTRUCTOR})   //表示这个注解只能用于方法前面和构造器前面
@Retention(value = RetentionPolicy.RUNTIME)  //可以被反射机制读取
@Inherited()
@Documented()
public @interface AnnotationCustomInterface {

    /**
     * 自定义注解元素，建议给予默认值，如果默认值为-1则表示不使用则不存在该注解元素
     */
    int nonUse() default -1;
    short age() default 0;
    String[] desc() default {};
    String name();   //不给默认值的注解元素必须填入


    /**
     * meta-annotation 元注解
     * 作用：注解其他注解，java提供了4个标准的元注解
     *
     * 1. @Target  -- 描述注解的使用范围
     *             package（包）
     *             type（类/接口/枚举/Annotation类型）
     *             constructor（构造器）
     *             field（成员属性）
     *             method（成员方法）
     *             local_variable（局部变量）
     *             parameter（方法参数）
     *
     * 2. @Retention  -- 用于描述注解的生命周期
     *              //编译期注解使用较多
     *                source（在源java文件中有效）
     *                class（在class文件中有效(即class保留)）
     *              //运行期注解使用较多
     *                runtime（运行时有效，可以被反射机制读取）
     *
     * 3. @Document  -- Documented注解表明这个注释是由 javadoc记录的，在默认情况下也有类似的记录工具。 如果一个类型声明被注释了文档化，它的注释成为公共API的一部分
     *
     *
     * 4. @Inherited  -- 是否父类的注解可以被子类继承，有@Inherited子类会自动继承此注解，否则的话，子类不会继承此注解（仅仅用于类上面有效）
     */
}
