package foundation;



/**
 * 创建线程的两种方式:
 *
 * 第一种方式是将类声明为 Thread 的子类
 * 1.定义Thread类的子类，并重写该类的run()方法，该run()方法的方法体就代表了线程需要完成的任务,因此把run()方法称为线程执行体
 * 2.创建Thread子类的实例，即创建了线程对象
 * 3.调用线程对象的start()方法来启动该线程。
 *
 * 第二种方式是声明一个类实现Runnable 接口
 * 1. 定义Runnable接口的实现类，并重写该接口的run()方法，该run()方法的方法体同样是该线程的线程执行体
 * 2. 创建Runnable实现类的实例，并以此实例作为Thread的target来创建Thread对象，Thread对象才是真正的线程对象
 * 3. 调用线程对象的start()方法来启动线程
 * Runnable优势
 * 1. 适合多个相同的程序代码的线程去共享同一个资源
 * 2. 可以避免java中的单继承的局限性
 * 3. 增加程序的健壮性，实现解耦操作，代码可以被多个线程共享，代码和数据独立
 * 4. 线程池只能放入实现Runnable或callable类线程，不能直接放入继承Thread的类
 */
public class MyRunnable /*extends Thread*/ implements Runnable {

    private Runnable runnable;

    public MyRunnable(Runnable r){
        this.runnable = r;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}