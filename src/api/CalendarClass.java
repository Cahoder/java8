package api;

import java.util.Calendar;
import java.util.Date;

public class CalendarClass {

    public void operationCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,6,19);
        System.out.println("今年是"+calendar.get(Calendar.YEAR));
        System.out.println("月份是"+(calendar.get(Calendar.MONTH)+1));   //西方从0开始计算月份
        System.out.println("天数是"+calendar.get(Calendar.DATE));

        calendar.set(Calendar.MONTH,8);
        Date time = calendar.getTime();    //将日历类转化成Date类
        System.out.println(time);

        Calendar calendar2 = Calendar.getInstance();   //记住这是一个单例模式
        System.out.println(calendar2.get(Calendar.YEAR));
    }
}
