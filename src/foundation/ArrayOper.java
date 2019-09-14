package foundation;

import java.lang.String;

public class ArrayOper {

    private String name;
    private int age;

    public static void createArray(){
        //动态初始化数组
        int[] dynamicArr = new int[300];
        //静态初始化数组
        int[] staticArr = new int[]{ 1,2,3,4,5,6,7,8,9 };
        String[] staticStringArr = {"hello", "world", "java"};

        char[] arr2 = new char[10];
        String[] arr3 = new String[10];

        for (int i = 0; i < dynamicArr.length;i++) {
            dynamicArr[i] = i;
        }

        for (int i = 0; i < staticArr.length;i++) {
            //System.out.println(staticArr[i]);
        }

        System.out.println("memory address is "+staticStringArr);
        System.out.println(arr2[0]);
        System.out.println('\u0000');
        System.out.println(arr3[0]);
    }

    public static void arrayReserve(){
        String[] strArr = {"you","love","much","very","I"};

        for (int min = 0,max = strArr.length-1; min < max; min++,max--) {
            String temp = strArr[min];
            strArr[min] = strArr[max];
            strArr[max] = temp;
        }

        printArrary(strArr);

    }

    public static void printArrary(String[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }

}
