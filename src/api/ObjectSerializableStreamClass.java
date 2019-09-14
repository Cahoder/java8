package api;

import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * 序列化对象：
 *      ObjectOutputStream  extends OutputStream
 *      将 Java 对象的基本数据类型和图形写入 OutputStream。 通过在流中使用文件可以实现对象的持久存储
 *      只能将支持 java.io.Serializable 接口的对象写入流中
 *
 *      void writeObject() 方法用于从流写入对象
 *
 * 反序列化对象：
 *      ObjectInputStream  extends InputStream
 *      ObjectInputStream 对以前使用 ObjectOutputStream 写入的基本数据和对象进行反序列化。
 *      只有支持 java.io.Serializable 或 java.io.Externalizable 接口的对象才能从流读取。
 *
 *      Object readObject() 方法用于从流读取对象
 *
 * Serializable Interface 属于标记型接口
 */
public class ObjectSerializableStreamClass implements Serializable {
    /**
     * TODO implements Serializable
     * 显式声明可序列化类拥有指定的序列编号，防止因为修改类JVM重新自动分配序列编号
     * 导致已经序列化对象进行反序列化失败（Throws InvalidClassException）
     */
    private static final long serialVersionUID = -42L;

    private String desc;
    private long time;
    private static String name = "guys";
    private transient short age;   //关键字 transient 能够阻止不想被序列化的成员变量和成员方法

    public ObjectSerializableStreamClass(){
        ObjectSerializableStreamClass.name = "boys! ";
        this.desc = "I am ObjectSerializableStreamClass !";
        this.age = 15;  //无效
        this.time = new Date().getTime();
    }

    public ObjectSerializableStreamClass(String desc){
        this.desc = desc;
        this.time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Hello, "+ name + desc + " My (transient) age cannot be Serialize forever : "+ age + " (DateTime : "+ time + ")";
    }

    //持久化对象到物理硬盘
    public void serializeObject(){
        File file = new File("./ObjectSerializableStream.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ObjectSerializableStreamClass());   //序列化自己
            oos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //重构对象到内存
    public void unSerializeObject(){
        try{
            FileInputStream fis = new FileInputStream("./ObjectSerializableStream.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();   //反序列化自己
            ois.close();
            System.out.println(obj.toString());
        }catch (IOException | ClassNotFoundException e ){
            e.printStackTrace();
        }
    }

    //序列化集合可以存储多个对象
    public void SerializeCollection(){
        ArrayList<ObjectSerializableStreamClass> list = new ArrayList<>();
        list.add(new ObjectSerializableStreamClass("I am Object One !"));
        list.add(new ObjectSerializableStreamClass("I am Object Two !"));
        list.add(new ObjectSerializableStreamClass("I am Object Three !"));
        list.add(new ObjectSerializableStreamClass("I am Object Four !"));
        list.add(new ObjectSerializableStreamClass("I am Object Five !"));

        //saveSerializeArrayList
        try(
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./ObjectSerializableStreamLists.txt"))
        ){
            oos.writeObject(list);
            oos.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

        //checkSerializeArrayList
        try(
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./ObjectSerializableStreamLists.txt"))
        ){
            list = (ArrayList<ObjectSerializableStreamClass>) ois.readObject();
            Iterator it = list.iterator();
            while (it.hasNext()){
                ObjectSerializableStreamClass obj = (ObjectSerializableStreamClass)it.next();
                System.out.println(obj.toString());
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
