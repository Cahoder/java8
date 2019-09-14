package apiSuper;

import java.lang.String;

/**
 * 注解编程 JDK >= 1.5
 *
 * 作用：1.对程序做出解释
 *      2.可以被其他程序读取
 */
//@AnnotationCustomInterface  //不允许放在这里
public class AnnotationClass {

    @Override
    //重写父类方法注解
    public String toString() {
        return "i am a to String";
    }

    @Deprecated
    //不建议使用方法注解
    protected void deprecateMethod() {

    }

    @SuppressWarnings(value = {"unchecked","fallthrough","all"})
    //抑制警告注解
    protected void suppressWarningsMethod(){
        switch (0){
            case 1:
            case 2:
        }
    }

    @AnnotationCustomInterface(name = "C.HDe")
    //自定义注解
    protected void customAnnotation(){

    }

}
