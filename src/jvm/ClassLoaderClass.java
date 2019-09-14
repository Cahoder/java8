package jvm;

import jvm.customClassLoader.ContextClassLoader;
import jvm.customClassLoader.DecryptClassLoader;
import jvm.customClassLoader.EncryptClassUtil;
import jvm.customClassLoader.FileSystemClassLoader;

/**
 * 深入JVM核心机制：类加载全过程
 *  A-加载(class文件字节码 --> *类加载器{ 类加载器的逻辑层次结构:树形结构 类加载器的代理模式:双亲委派机制 java.class.classLoader类作用 } --> 进入对应的内存区域准备被链接)
 *  B-链接(b1.验证 b2.准备 b3.解析)
 *  C-初始化(类构造器<clinit>() -->  { 主动引用&&被动引用 })
 *
 *  通常当你需要动态加载资源的时候，可以有三种classLoader可以选择：
 *      1.系统类加载器 （application|system classLoader）
 *      2.当前类加载器
 *      3.当前线程类加载器
 */
public class ClassLoaderClass {

    //测试自定义线程上下文类加载器
    public void customContextClassLoaderTest(){
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();//获取当前线程的上下文类加载器
        System.out.println(threadClassLoader);  //默认是系统类加载器sun.misc.Launcher$AppClassLoader@18b4aac2
        ContextClassLoader loader = new ContextClassLoader("./");
        Thread.currentThread().setContextClassLoader(loader);//改变当前线程的上下文类加载器
        System.out.println(Thread.currentThread().getContextClassLoader());//jvm.customClassLoader.ContextClassLoader@6d6f6e28

        try {
            Class<?> helloWorld = loader.loadClass("HelloWorld");
            //由于该类加载器不使用双亲委派机制，所以改变了它的上下文类加载器为jvm.customClassLoader.ContextClassLoader@6d6f6e28
            //如果该类加载器还是遵循双亲委派机制的话，那么它的上下文类加载器为sun.misc.Launcher$AppClassLoader@18b4aac2（因为系统加载器可以加载该类，又会修改掉上下文类加载器）
            System.out.println(helloWorld.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    //测试自定义加密解密类加载器
    public void customDecryptClassLoaderTest(){
        EncryptClassUtil encrypt = new EncryptClassUtil();
        encrypt.Encrypt("./HelloWorld.class","./src/jvm/HelloWorld.class");
        try {
            //普通文件类加载器无法加载加密类
            //Exception in thread "main" java.lang.ClassFormatError: Incompatible magic value 889275713 in class file HelloWorldEncrypt
            //FileSystemClassLoader fsclA = new FileSystemClassLoader("./src/jvm/");
            //Class<?> a1Class = fsclA.loadClass("HelloWorld");

            //使用解密文件类加载器进行加载
            DecryptClassLoader dcl = new DecryptClassLoader("./src/jvm/");
            Class<?> a1Class = dcl.loadClass("HelloWorld");
            System.out.println(a1Class + " " + a1Class.getClassLoader());   //class HelloWorld jvm.customClassLoader.DecryptClassLoader
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //测试自定义文件加载器
    public void customFileClassLoaderTest(){
        FileSystemClassLoader fsclA = new FileSystemClassLoader("./");
        FileSystemClassLoader fsclB = new FileSystemClassLoader("./");
        try {
            Class<?> a1Class = fsclA.loadClass("HelloWorld");
            Class<?> a2Class = fsclA.loadClass("HelloWorld");
            Class<?> a3Class = fsclB.loadClass("HelloWorld");
            Class<?> a4Class = fsclB.loadClass("java.lang.String");
            if (a1Class.hashCode() == a2Class.hashCode()){
                System.out.println("注意事项一：由同一个加载器对象加载同一个类，JVM会视为是同一个类对象");
            }
            if (a1Class.hashCode() != a3Class.hashCode()){
                System.out.println("注意事项二：由不同的加载器对象加载同一个类，JVM会视为不是同一个类对象");
            }

            //由于自定义文件加载器遵循了双亲委托机制，所以核心库肯定是由引导类加载器加载的
            if (a4Class.getClassLoader() == null)
                System.out.println("遵循了双亲委托机制的自定义加载器 java.lang.String 类被 Bootstrap classLoader 加载!");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //类加载器
    public void classLoader(){
        System.out.println("----------------------------------查看类加载器层次结构----------------------------------");
        System.out.println("当前类的加载路径是："+System.getProperty("java.class.path"));
        System.out.println("当前类的加载器是："+ClassLoader.getSystemClassLoader());   //sun.misc.Launcher$AppClassLoader@18b4aac2 (系统)应用程序类加载器
        System.out.print("当前类的加载器的父类加载器是："+ClassLoader.getSystemClassLoader().getParent()); //sun.misc.Launcher$ExtClassLoader@7f31245a 扩展类加载器
        System.out.print("-->"+ClassLoader.getSystemClassLoader().getParent().getParent()); //null  扩展类加载器上一层是引导类加载器(由c语言编写)，所以扩展类加载器已经没有父类加载器了

        System.out.println("\n");
        System.out.println("----------------------------------类加载器的代理模式:双亲委派机制----------------------------------");
        System.out.println("当前类加载器遇到了类加载请求，会先委托给父类加载，最终委托到根类；");
        System.out.println("如果父类可以加载则返回类加载后的class实例，只有父类无法加载类时候才自己加载；");
        System.out.println("自己也加载不了，最终会throw ClassNotFoundException；");
        System.out.println("作用：安全至上，保证用户无法重写核心类库已经有的类！");//即使自己自定义了一个java.lang.String类，类加载器最终也会加载核心类库里面的String类

        System.out.println("\n----------------------------------java.class.classLoader类作用----------------------------------");
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        try {
            //动态加载类
            Class<?> aClass = appClassLoader.loadClass("jvm.ClassLoaderClass");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //类初始化
    //类构造器：由编译器自动收集类中所有的 "类变量"赋值动作和静态代码块 中的语句合并产生
    public void clinit(){

        /**
         * 主动引用：一定会发生类的初始化动作
         * 1.new一个类的对象
         * 2.调用类的静态成员（除了final常量）和静态方法
         * 3.使用java.lang.reflect对类进行反射调用
         * 4.main方法所在的类一定会被初始化
         * 5.初始化一个类，如果其父类没有被初始化，则会先初始化其父类（所以Object类一定会被初始化）
         */


        /**
         * 被动引用：不会发生类的初始化
         * 1.调用类的常量（final修饰的） （例：public static final int num = 10;）
         * 2.通过数组定义类的引用 （例：classA arrs = new classA[10]; 不会发生类的初始化）
         * 3.当访问一个静态变量，只有真正声明这个静态变量的类才会被初始化（例：通过子类访问父类的静态变量，父类会被初始化，子类不会初始化）
         */


        /**
         * 类缓存
         * 类一旦被加载会被缓存一段时间，因此不会多次进行类初始化，但是java垃圾回收机制可以视情况对类的java.lang.class对象进行垃圾回收
         */
    }

}
