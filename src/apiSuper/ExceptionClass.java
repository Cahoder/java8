package apiSuper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 异常体系
 * 超级父类 Throwable  ==>  包含了 Error 和 Exception
 * error： 严重错误，不可以进行抛出或者捕获，必须避免或者修改源码
 * Exception: 分为 RuntimeException 和 编译期异常
 *
 *  RuntimeException (可以不进行处理，最终会交给JVM进行打印，中断程序)
 *
 *
 *  编译期异常 必须解决
 *
 */
public class ExceptionClass {
    public static void exceptionOperation(){

        int i[] = new int[]{1,2,3,4,5};
        ArrayList<Integer> al = new ArrayList<>();
        Collections.addAll(al,1,2,3,4,5);

        try{
            System.out.println(i[5]);

        } catch (ArrayIndexOutOfBoundsException e){
            //子类异常必须放在父类异常之前 否则会出现报错
            System.out.println("超出数组边界");
            e.printStackTrace();

            return;
        } catch (IndexOutOfBoundsException e){
            System.out.println("超出索引边界");
            e.printStackTrace();

            return;
        } finally {
            System.out.println("资源释放");

            //return; //finally中有return那么永远都只会返回finally里面的结果，应该避免
        }

        System.out.println("后续的代码");
    }
}

