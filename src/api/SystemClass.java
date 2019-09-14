package api;

import java.util.Arrays;

public class SystemClass {

    //获取当前时间距离时间元年之间的毫秒值
    public void getTimeMillis(){
        long beforeTime = System.currentTimeMillis();
        for (int i = 0; i < 10000;i++) {
            System.out.println(i);
        }
        long afterTime = System.currentTimeMillis();

        System.out.println("一万次循环耗时："+(afterTime-beforeTime)+"毫秒");
    }

    //地址复制数组到目标数组
    public void copyArray(){
        int[] rawArr = new int[]{1,2,3,4,5,6};
        int[] targetArr = new int[]{1,2,3,4,5,6};

        System.arraycopy(rawArr,3,targetArr,0,3);
        System.out.println(Arrays.toString(targetArr));
    }
}
