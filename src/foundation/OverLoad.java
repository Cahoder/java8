package foundation;

/**
 * java 不支持函数参数默认值 因为会和重载冲突
 *
     例子：假设 Java 支持默认参数，则对于下面的代码
     public int add(int a) {
         // do something
     }
     public int add(int a, int b = 0) {
          // do something
     }
     当执行 `add(12)` ,你说是调用的 `int add(int a)` 还是 `int add(int a, int b = 0)` ?
 *
 * */


public class OverLoad {
    public static int sum(int a, int b){
        return a+b;
    }

    public static int sum(double a, int b){
        return (int)a+b;
    }

    public static int sum(int a, double b){
        return a+(int)b;
    }

    //这不是重载
    public static int SUM(int a, int b){
        return a+b;
    }

    public static int sum(int a,int b,int c){
        return a+b+c;
    }

//    void int sum(int a, int b){
//        return a+b;
//    }

//    static int sum(int a, int b){
//        return a+b;
//    }

//    public int sum(int a, int b){
//        return a+b;
//    }
}
