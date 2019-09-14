package api;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * java.io.PrintStream extends OutputStream;
 *  1.只输出不读取
 *  2.不会抛出 IOException （但是会抛出 FileNotFoundException ）
 *  3.特有方法 Print 和 Println
 *
 *  注意：
 *      1.使用父类的Write()写数据，查看数据会查询编码表
 *      2.使用特有的Print()|Println(),查看数据原样输出不会改变
 *
 *  System.out.print() 中的out成员属性 ->  public final static PrintStream (System.)out = null; //控制台打印本身就是一个打印流对象
 */
public class PrintStreamClass {
    public void outPrintStream(){
        PrintStream ps = null;
        try {
            ps = new PrintStream("./a.txt");
            ps.println(97);
            ps.write(97);
            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    //改变打印流方向 （默认控制台打印）
    public void outPrintStreamToLogs(){
        PrintStream ps = null;
        try {
            System.out.println("我现在打印在控制台！");
            ps = new PrintStream("./a.txt","GBK");
            System.setOut(ps); //改变输出方向


            System.out.println("我现在打印在指定的日志文件里咯！");
            System.out.println(true);
            ps.println(1997);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
}
