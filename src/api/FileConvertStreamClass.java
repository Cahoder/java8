package api;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.lang.String;
import java.util.Random;

public class FileConvertStreamClass {
    public void FileInputStreamReader(){
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("./a.txt"),"GBK");
            int validLen;
            char[] chars = new char[1024];
            while ((validLen = isr.read(chars))!=-1){
                System.out.println(new String(chars,0,validLen));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void FileOutputStreamWriter(){
        try {
            OutputStreamWriter isw = new OutputStreamWriter(new FileOutputStream("./a.txt"),"GBK");
            char[] chars = new char[1024];
            Random rand = new Random();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) rand.nextInt(256);
            }
            for (int i = 0; i < chars.length ; i++) {
                isw.write(chars,0,rand.nextInt(chars.length) +1);
                isw.write(10);
                isw.flush();
            }
            isw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void FileCodingConvert(){
        try (
            InputStreamReader isr = new InputStreamReader(new FileInputStream("./a.txt"),"GBK");
            OutputStreamWriter isw = new OutputStreamWriter(new FileOutputStream("./b.txt"), StandardCharsets.UTF_8);
        ){
            int validLen;
            char[] chars = new char[1024];
            while ((validLen = isr.read(chars))!=-1){
                isw.write(chars,0,validLen);
                isw.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
