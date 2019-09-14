package apiSuper;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.String;

/**
 * java 动态编译
 *
 * 1.（JavaCompiler -- JDK1.6++）
 * javax.tools.JavaCompiler
 *
 * 2.（Runtime -- JDK1.6--）
 * java.java.lang.Runtime
 */
public class DynamicCompileClass {

    //--JDK1.6之前：是通过Runtime调用getRuntime()，启动新的进程去操作
    public void RuntimeExecute(){
        Runtime run = Runtime.getRuntime();
        Process proc = null;
        byte[] bytes = new byte[1024];
        int len;
        try {
            //调用系统命令
            String[] cmd={"/bin/sh","-c","ls -al"};
            proc = run.exec(cmd);
            InputStream cmdStream = proc.getInputStream();
            while ((len = cmdStream.read(bytes))!=-1){
                System.out.println(new String(bytes,0,len));
            }
            cmdStream.close();


            //编译java文件Runtime
            String [] compileJava={"/bin/sh","-c","javac -cp /home/cahoder/IdeaProjects/java8/ HelloWorld.java"};
            proc = run.exec(compileJava);
            InputStream javaCStream = proc.getInputStream();
            while ((len = javaCStream.read(bytes))!=-1){
                System.out.println(new String(bytes,0,len));
            }
            javaCStream.close();

            //执行java字节码
            String [] javaCompiles={"/bin/sh","-c","java -cp /home/cahoder/IdeaProjects/java8/ HelloWorld"};
            proc = run.exec(javaCompiles);
            InputStream javaStream = proc.getInputStream();
            while ((len = javaStream.read(bytes))!=-1){
                System.out.println(new String(bytes,0,len));
            }
            javaStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (proc != null) proc.destroy();
        }
    }


    //JDK1.6++ 提供了JavaCompiler工具进行动态编译
    public void JavaCompilerExecute(){
        /**
         * @param in "standard" input; use System.in if null  --不指定输入流则使用系统控制台
         * @param out "standard" output; use System.out if null  --不指定输出流则使用系统控制台
         * @param err "standard" error; use System.err if null  --不指定错误输出流则使用系统控制台
         * @param arguments arguments to pass to the tool  --需要编译的源代码文件，可变参数
         */
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        int result = jc.run(null,null,null,"/home/cahoder/IdeaProjects/java8/HelloWorld.java");
        System.out.println(result == 0 ? "编译成功！":"编译失败！");

        /**
         * 可以搭配使用类加载ClassLoader和反射编译对.class文件进行执行
         */
        String javaClassDir = "home/cahoder/IdeaProjects/java8/";
        try {
            URL[] urls = new URL[]{new URL("file:/"+ javaClassDir)};

            URLClassLoader loader = new URLClassLoader(urls);             //动态运行加载类文件
            Class<?> helloWorldClass = loader.loadClass("HelloWorld");
            Method main = helloWorldClass.getMethod("main", String[].class);
            main.invoke(null,(Object)new String[]{});  //静态方法可以不需要指定obj,但是必须把参数强转Object类型

        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
