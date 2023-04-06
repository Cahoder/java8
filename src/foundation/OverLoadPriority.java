package foundation;

import java.io.Serializable;

/**
 * 方法重载优先级
 **/
public class OverLoadPriority {

    /**
     * 基础类型会向上转型
     * 但是不会向下转型
     */

    //char cast byte unsafe 不会使用此重载
    public static void sayHello(byte a) { System.out.println("hello byte"); }

    //char cast short unsafe 不会使用此重载
    public static void sayHello(short a) { System.out.println("hello short"); }

    //优先级(最高)   char>int>long>float>double
    public static void sayHello(char a) { System.out.println("hello char");}

    public static void sayHello(int a) { System.out.println("hello int"); }

    public static void sayHello(long a) { System.out.println("hello long"); }

    public static void sayHello(float a) { System.out.println("hello float"); }

    public static void sayHello(double a) { System.out.println("hello double"); }

    /**
     * 基础类型无法找到合适重载
     * 只会找基础类型对应的包装类型重载
     */
    public static void sayHello(Character a) { System.out.println("hello Character"); }

    /**
     * 包装类型不会向上转型寻找
     * 只会找包装类型的父类型和接口类型
     */
    // 不会使用此重载
    public static void sayHello(Integer a) { System.out.println("hello Integer"); }

    /**
     * 如果存在包装类型上级同级重载
     * 会拒绝编译提示Type-Ambiguous
     * 除非调用方显式指明 例如:sayHello((Comparable<Character>)'a'); 或者 sayHello((Serializable)'a');
     */
    public static void sayHello(Serializable a) { System.out.println("hello Serializable"); }
    public static void sayHello(Comparable<Character> a) { System.out.println("hello Comparable"); }

    //无实现的父类接口重载则会使用Object根类
    public static void sayHello(Object a) { System.out.println("hello Object"); }

    /**
     * 可见变长参数的重载优先级是最低的
     * 可变参数同样只会上转型不会下转型
     */
    public static void sayHello(char... a) { System.out.println("hello char..."); }

    public static void sayHello(int... a) { System.out.println("hello int..."); }

    /**
     * 同样只会使用对应的可变参数包装类
     * 同样依据 父类型|接口类型可变参数->Object可变参数
     */
    public static void sayHello(Character... a) { System.out.println("hello Character..."); }

    // 不会使用此重载
    public static void sayHello(Integer... a) { System.out.println("hello Integer..."); }

    //同级可变参数调用方必须显式指明 -> 否则拒绝编译提示Type-Ambiguous
    public static void sayHello(Serializable... a) { System.out.println("hello Serializable..."); }
    public static void sayHello(Comparable<Character>... a) { System.out.println("hello Comparable..."); }

    public static void sayHello(Object... a) { System.out.println("hello Object..."); }

    public static void main(String[] args) {
        sayHello('a');
    }

}
