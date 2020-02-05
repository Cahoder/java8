package api;

import java.lang.String;
import java.util.ArrayList;

/**
 * 包装类
 */
public class WrapClass {

    //将一个基本数据类型的值放到一个对象中进行处理
    public void integerWarp(){

        //以下两种写法都可以
        Integer int1 = new Integer(100);
        Integer int2 = Integer.valueOf(100);


        //JDK1.5++   支持自动装箱拆箱
        System.out.println("自动拆箱结果值 " + int1 + "  == 手动拆箱结果值 " + int2.intValue());

        Integer res = int1 + int2;   //这里先是进行了自动拆箱然后自动装箱
        System.out.println(res);

        ArrayList<Integer> ali = new ArrayList<>();
        ali.add(3);  //自动装箱   === ali.add(Integer.valueOf(3))
        System.out.println("这里也发生了自动装箱："+ali);

    }


    public void elementDataToString(){
        //基本数据类型  ==>  String类型
        //第一种: 基本数据类型值 + ""
        String str1 = 100 + "";
        //第二种: 包装类的 static String toString(基本数据类型值)
        String str2 = Integer.toString(100);
        //第三种: String类的 static String valueOf(基本数据类型值)
        String str3 = String.valueOf(100);
        System.out.println(str1 + str2 + str3);  //100100100

        //String类型  ==>  基本数据类型
        //包装类的 static XXX(基本数据类型) parseXXX(String s)
        int int1 = Integer.parseInt(str1);
        int int2 = Integer.parseInt(str2);
        int int3 = Integer.parseInt(str3);
        System.out.println(int1 + int2 + int3);  //300
    }

}
