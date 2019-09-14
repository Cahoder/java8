package foundation;


import java.lang.String;

/**
 * Lambda 使用前提
 * 1.必须具备接口，且要求接口有且仅有一个接口/抽象方法
 * 2.使用lambda必须具备上下文推断：也就是方法的参数或局部变量类型必须为lambda对应的接口类型，才能使用lambda作为接口的实例
 * 备注：有且仅有一个抽象方法的接口，成为“函数式接口”
 *
 *
 * 格式: (参数类型 ...args) -> { 方法体 }
 * 省略格式:
 * 1. (...args) -> { 方法体 }
 * 2. (...args) ->  方法体    //前提是方法体只有一行的时候 ({},return,;)  要省略必须一起省略
 * 3. arg -> {方法体}         //前提是参数列表只有一个时候
 * 4. arg -> 方法体          //前提是参数列表只有一个和方法体只有一行的时候 ({},return,;)  要省略必须一起省略
 *
 * 注：1.函数式接口作为方法的参数
 *      new Thread(Runnable task).start();
 *    2.函数式接口作为方法的返回值
 *      Collections.sort(ArrayList<Object>, (Object A, Object B) -> {return A - B;})
 */
@FunctionalInterface
public interface lambdaClass {
    //void calculator();
    //void calculator(int a, int b);
    int calculator(int a, String b);

}
