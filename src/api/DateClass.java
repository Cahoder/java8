package api;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.util.Date;
import java.util.Scanner;


public class DateClass {


    public void getDate(){

        //中国属于东八区 CST == GTM+8  （英国格林威治时间）
        //起始时间1970-01-01 00：00：00  == 0毫秒
        //

        /*
         * 获取无参数构造的日期类
         * */
        Date getNowDate = new Date();
        System.out.println("无参数构造的日期类"+getNowDate);

        /*
         * 获取当前时间距离格林威治起始时间多少毫秒
         * 以下两种调用相同
         * */
//        System.out.println(getNowDate.getTime());
//        System.out.println(System.currentTimeMillis());

        /*
         * 获取有参数构造的日期类
         * */
        Date getAppointDate = new Date(0L);
        System.out.println("有参数构造的日期类"+getAppointDate);


        /*
        * 将Date 类型格式化成为指定格式的字符串
        * */
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formats = sdf.format(getNowDate);
            System.out.println("格式化日期："+ formats);
        }catch (IllegalArgumentException exception){
            System.out.println("格式化参数异常:" + exception.getMessage());
        }


        /*
         * 将指定格式的字符串转化成为Date类型，如果格式解析失败将会抛出exception
         * */
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDateStr = "2019-07-20 14:45:48";
//            Date parseDate = sdf.parse(getNowDate.toString());
            Date parseDate = sdf.parse(formatDateStr);
            System.out.println("解析格式化日期："+ parseDate);
        }catch (ParseException exception){
            System.out.println("解析参数异常:" + exception.getMessage());
        }
    }

    //计算你活了多少天
    public void calculateBirthDay() {
        Date nowDate = new Date();
        Date birthDate = new Date();
        long nowDateTimeMillis = nowDate.getTime();

        System.out.println("请输入您的生日：（格式为YYYY-MM-DD）");
        String getBirthDay = new Scanner(System.in).next();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthDate = sdf.parse(getBirthDay);
        }catch (ParseException exception){
            System.out.println("输入的生日格式不正确请重新输入!");
            calculateBirthDay();
        }

        long birthDateTimeMillis = birthDate.getTime();

        //计算之间的毫秒值
        long betweenDateTimeMills = nowDateTimeMillis - birthDateTimeMillis;

        double secs = Math.floor(betweenDateTimeMills/1000);
        double mins = Math.floor(secs/60);
        double hours = Math.floor(mins/60);
        double days = Math.floor(hours/24);


        System.out.printf("恭喜你已经在地球上存活了%f秒 === %f分 === %f小时 === %f天",secs,mins,hours,days);

    }
}
