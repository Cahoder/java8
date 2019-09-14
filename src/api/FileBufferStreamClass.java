package api;

import java.io.*;
import java.lang.String;

/**
 * 缓冲流:通过一个内置缓冲数组一次读取多字符/字节流数据存储到缓冲数组中，提高读写效率
 *
 */
public class FileBufferStreamClass {

   //通过缓冲数组读取字节流数据
   public void FileBufferedInputStream(){
       try {
           FileInputStream fis = new FileInputStream("./c.txt");
           BufferedInputStream bis = new BufferedInputStream(fis,1024);

           int validLen;
           byte[] bytes = new byte[1024];
           while ((validLen = bis.read(bytes))!=-1){  //理论上来说最大可读 1024组字节流数据，每个数据里面包含了一个1024大小的字节数组
               System.out.println(new String(bytes,0,validLen));
           }
           bis.close();
       }catch (IOException e){
           e.printStackTrace();
       }
   }

   //通过缓冲数组写入字节流数据
   public void FileBufferedOutputStream(){
       BufferedOutputStream bfos = null;
       try {
           FileOutputStream fos = new FileOutputStream("./e.txt");
           bfos = new BufferedOutputStream(fos,2);
           byte[] bytes = new byte[1024];
           for (int i = 1; i <= 10240; i++) {
               bfos.write(bytes,0,bytes.length);   //写1024*1024*10
           }
       }catch (IOException e){e.printStackTrace();}
        finally {
           try {
               if (bfos!=null) bfos.close();
           } catch (IOException e) { e.printStackTrace(); }
       }
   }

   //通过缓冲数组写入字符流数据
   public void FileBufferedCharacterWriter(){
       BufferedWriter bw = null;
       try {
           FileWriter fw = new FileWriter("./e.txt");
           bw = new BufferedWriter(fw,2);
           char[] chars = new char[1024];
           for (int i = 0; i < chars.length; i++) {
               chars[i] = '德';  //utf8一个中文三个字节
           }

           for (int i = 1; i <= 10240; i++) {
               bw.write(chars,0,chars.length);   //写1024*1024*10*3
               bw.newLine();   //相当于换行符
           }
       } catch (IOException e){ e.printStackTrace(); }
         finally {
           try {
               if (bw!=null) bw.close();
           } catch (IOException e) { e.printStackTrace(); }
       }
   }

   //通过缓冲数组读取字符流数据
   public void FileBufferedCharacterReader(){
       try {
           FileReader fr = new FileReader("./e.txt");
           BufferedReader bfr = new BufferedReader(fr,2);

           String validLine;
           //readLine()读取一行但是不会包括换行符
           while ((validLine = bfr.readLine())!=null){  //理论上来说最大可读 2行字符串
               System.out.println(validLine);
           }
           bfr.close();
       }catch (IOException e){
           e.printStackTrace();
       }
   }

   //比较使用缓存流和不使用缓存流的写入速度
    public void FileBufferedWriterTester(){
        try (
                FileWriter NoBufferWriter = new FileWriter("./e.txt");
                FileWriter BufferWriter = new FileWriter("./f.txt");
                BufferedWriter bfw = new BufferedWriter(BufferWriter,2);
             ){

            char[] chars = new char[1024];
            for (int i = 0; i < chars.length; i++) { chars[i] = '德';}  //utf8一个中文三个字节
            long startTimeMillis;
            long endTimeMillis;

            //---------------------------------------------------------------
            System.out.println("测试不使用缓存流写入30M的TXT文件：");
            startTimeMillis = System.currentTimeMillis();
            for (int i = 1; i <= 10240; i++) {
                NoBufferWriter.write(chars,0,chars.length);   //写1024*1024*10*3
            }
            endTimeMillis = System.currentTimeMillis();
            System.out.println("耗时："+(endTimeMillis-startTimeMillis)+"毫秒");



            //---------------------------------------------------------------
            System.out.println("测试使用缓存流写入30M的TXT文件：");
            startTimeMillis = System.currentTimeMillis();
            for (int i = 1; i <= 10240; i++) {
                bfw.write(chars,0,chars.length);   //写1024*1024*10*3
            }
            endTimeMillis = System.currentTimeMillis();
            System.out.println("耗时："+(endTimeMillis-startTimeMillis)+"毫秒");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
