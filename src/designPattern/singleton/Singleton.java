package designPattern.singleton;

/**
 * 单例模式
 */
public class Singleton {

    /*饿汉式*/
    /*
    //创建 SingleObject 的一个对象
    private static final Singleton instance = new Singleton();
    //让构造函数为 private，这样该类就不会被实例化
    private Singleton(){}
    //获取唯一可用的对象
    public static Singleton getInstance(){
        return instance;
    }
    */

    /*懒汉式*/
    private static Singleton instance;
    private Singleton (){}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * 多线程进阶版
     * @see juc.SinglePattern
     */
}
