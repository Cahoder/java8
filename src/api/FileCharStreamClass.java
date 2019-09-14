package api;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;


/**
 * 以字符为单位进行流操作
 */
public class FileCharStreamClass {

    public void FileCharacterWriter(){
        FileWriter fw = null;
        try {
//            fw = new FileWriter("./c.txt");
            fw = new FileWriter("./c.txt",true);

            for (int i = 0; i < 10; i++) {
                //write() 将字符流 转化成 字节流 存储到 内存缓冲区 还未真正写入内存
                fw.write(new char[]{'我','叫','蔡','宏','德'});
                fw.write("我真的好帅气啊！");
                fw.write(10); //换行符 \n
                fw.flush();  //清除内存缓冲区 真正的将文件刷入硬盘中去
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fw != null){
                try {
                    fw.close();  //关闭资源之前会执行一次 flush()
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void FileCharacterReader(){
        //jdk7 新写法
        //会自动执行 close()  释放资源
        try(
            FileReader fr = new FileReader("./c.txt")
        ){
            int validLen = 0;
            char[] bufferChar = new char[1024];

            while ((validLen = fr.read(bufferChar))!= -1){
                System.out.println(new String(bufferChar,0,validLen));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
