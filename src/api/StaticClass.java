package api;

import java.lang.String;

public class StaticClass {
    static {
        //静态代码块
        //无论new多少个对象，永远只会执行一次
        StaticClass.setRoom();
    }

    private static void setRoom(){
        StaticClass.room = "高三1班";
        StaticClass.idCounter = 0;
    }

    private static String room;
    private static int idCounter;
    private String name;
    private int age;
    private int id;

    public StaticClass (){
        this.id = ++StaticClass.idCounter;
    }

    //一切的类都是继承于Object基类
    //都存在一些父类方法可以进行重写
    @Override
    public String toString() {
        return "StaticClass{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    public StaticClass(String name, int age) {
        this.name = name;
        this.age = age;
        this.id = ++StaticClass.idCounter;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void getInfo(){
        System.out.println("学号："+id+",姓名："+name+",年龄："+age+",班级："+room+" 前来报道！");
    }
}
