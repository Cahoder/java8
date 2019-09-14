package api;

import java.lang.String;
import java.util.Arrays;




public class StringClass {

    public void createString(){
        String str1 = "abc";   //字符串的底层是一个被final修饰的byte[]数组所以一旦生成就不可改变
        String str2 = new String(new char[]{'a','b','c'});
        String str3 = new String(new byte[]{97,98,99});
        String str4 = "abc";
        String str5 = "Abc";


        System.out.println(str1==str4); //放在堆常量池中
        System.out.println(str1==str2); //放在堆中
        System.out.println(str2==str3); //放在堆中

        System.out.println("==============================");

        System.out.println(str1.equals(str2));
        System.out.println(str2.equals(str3));
        System.out.println(str3.equalsIgnoreCase(str5));
    }

    public void operString(){
        String str = "fasfhdsjfsdifopewjfnklsjafkldas";

        //获取字符串长度
        int length = str.length();
        System.out.println("字符串长度为："+length);

        //拼接字符串成为新的字符串
        String newStr = str.concat("448115311");
        System.out.println("新的拼接字符串为："+newStr);

        //获取字符串指定索引的字符
        char getChar = str.charAt(10);
        System.out.println("下标为10索引的字符为："+getChar);

        //获取目标字符串在源字符串中出现的第一次位置，找不到为-1
        String targetStr = "fsd";
        int pos = str.indexOf(targetStr);
        System.out.println("目标字符串在源字符串中出现的第一次位置为："+pos);

        //截取字符串
        String subStr1 = str.substring(2);
        System.out.println("源字符串截取到字符串结尾为："+subStr1);
        String subStr2 = str.substring(2,5);
        System.out.println("源字符串截取到指定索引范围为："+subStr2);

        //将字符串转成字符数组
        char[] chars = str.toCharArray();
        Arrays.sort(chars);  //默认进行升序排序，字符按照字母排序
        System.out.println(Arrays.toString(chars));
//        for (int i = 0; i < chars.length; i++) {
//            System.out.println(chars[i]);
//        }
        //将字符串转成byte数组
        byte[] bytes = str.getBytes();
        Arrays.sort(bytes);
        System.out.println(Arrays.toString(bytes));
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.println(bytes[i]);
//        }

        //替换字符串中的源字符（串） 为 目标字符（串）
        String newStr1 = str.replace('a','z');
        System.out.println("源字符替换目标字符结果为："+newStr1);
        String newStr2 = str.replace("fsd","sss");
        System.out.println("源字符串替换目标字符串结果为："+newStr2);


        //字符串的分割方法
        String[] split1 = str.split("d");
        System.out.println("源字符串进行分割的结果为："+ Arrays.toString(split1));
        String[] split2 = str.split("d",2);
        System.out.println("源字符串进行分割的并且限制分割长度结果为："+ Arrays.toString(split2));
    }
}
