package api;

import java.io.*;
import java.lang.String;
import java.util.Properties;
import java.util.Set;

/**
 * Properties extends HashTable
 * 默认键值对都是字符串类型<String key,String value>
 * --- key 不可重复
 * --- value 可以重复
 * ---唯一一个可以配合IO流持久化集合数据
 *
 *     load(FileInputStream input)          注：读取中文会乱码（默认是使用unicode编码读取）
 *     load(FileReader read)
 *
 *     store(FileOutputStream output,String comment)        注：写入中文会乱码（默认是使用unicode编码写入）
 *     store(FileWrite write,String comment)
 */
public class PropertiesClass {
    public void storeKeyValueToFile(){
        Properties prop = new Properties();
        try(
            FileWriter writer = new FileWriter("./d.txt");
            FileOutputStream fos = new FileOutputStream("./d.txt",true)  //会转化成unicode编码
        ) {
            prop.setProperty("蔡宏德1","帅气！");
            prop.setProperty("蔡娟娟1","丑陋！");
            prop.setProperty("蔡宏德2","帅气！");
            prop.setProperty("蔡娟娟2","丑陋！");
            prop.setProperty("蔡宏德3","帅气！");
            prop.setProperty("蔡娟娟3","丑陋！");
            prop.setProperty("蔡宏德4","帅气！");
            prop.setProperty("蔡娟娟4","丑陋！");
            prop.setProperty("蔡宏德5","帅气！");
            prop.setProperty("蔡娟娟5","丑陋！");
            prop.store(writer,"this is a comment describe");

            prop.setProperty("蔡宏德6","帅气！");
            prop.setProperty("蔡娟娟6","丑陋！");
            prop.setProperty("蔡宏德7","帅气！");
            prop.setProperty("蔡娟娟7","丑陋！");
            prop.setProperty("蔡宏德8","帅气！");
            prop.setProperty("蔡娟娟8","丑陋！");
            prop.setProperty("蔡宏德9","帅气！");
            prop.setProperty("蔡娟娟9","丑陋！");
            prop.setProperty("蔡宏德10","帅气！");
            prop.setProperty("蔡娟娟10","丑陋！");
            prop.store(fos,"this is another comment describe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyValueFromFile(){
        Properties prop = new Properties();
        try {
            FileReader reader = new FileReader("./d.txt");
            prop.load(reader);
            reader.close();

            Set<String> keys = prop.stringPropertyNames();
            for (String key : keys) {
                String value = prop.getProperty(key);
                System.out.println(key+"="+value);
            }

            System.out.println("\n-------------使用字节流读取中文会乱码----------------\n");

            FileInputStream fis = new FileInputStream("./d.txt");
            prop.load(fis);

            keys = prop.stringPropertyNames();
            for (String key : keys) {
                String value = prop.getProperty(key);
                System.out.println(key+"="+value);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
