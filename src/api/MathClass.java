package api;

public class MathClass {

    public static void getMathUtils(){
        System.out.println("向上取整：Math.ceil(3.5) = "+ Math.ceil(3.5));
        System.out.println("向下取整：Math.ceil(3.5) = "+ Math.floor(3.5));
        System.out.println("四舍五入：Math.ceil(3.5) = "+ Math.round(3.5));
        System.out.println("获取绝对值：Math.abs(-3.5) = "+ Math.abs(-3.5));
        System.out.println("Java 提供的圆周率为 = "+ Math.PI);
    }

}
