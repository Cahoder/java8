package api;

import java.lang.String;

public class StringBuilderClass {

    /*
    *普通的String类 进行操作会在内存中产生很多的新字符串影响效率
    *
    * StringBuilder类 非线程安全    VS    StringBuffer类 线程安全
    * 通过利用字符串内存缓冲区，可以提高字符串操作的效率（看成一个长度可变的字符串）
    * 底层也是一个byte[]数组，但是没有被final关键字修饰，可以改变长度
    * 默认byte【】 value = new byte[16]  ==》   超出长度会自动扩容
    * */


    public void appendStr(){

        String str = "wow ";

        StringBuilder sb = new StringBuilder(str);
        sb.append("Hello ").append("world ").append(true).append(1).append('中');   //append返回对象本身，所以允许链式操作

//        str = str + "Hello "+"world "+true+1+'中';
        str = sb.toString();   // 效率高于上一种
        System.out.println(str);
    }


}
