package juc;

import java.lang.reflect.Constructor;

/**
 * 单例模式进阶
 */
public class SinglePattern {

    //懒汉式
    public static class LazySingle{
        private LazySingle(){}
        private static LazySingle single;
        public static LazySingle getInstance(){
            if (single == null) {
                single = new LazySingle();
            }
            return single;
        }
    }

    /**
     *  懒汉式 -- DCL式(Double Check Lock)
     *  为了解决在多线程环境下 single = new LazySingle() 被执行多次
     */
    public static class DCLLazySingle {
        //还是不安全,因为可以使用反射获取实例
        private DCLLazySingle(){}
        private volatile static DCLLazySingle single;
        public static DCLLazySingle getInstance() {
            if (single == null){
                synchronized (DCLLazySingle.class) {
                    if (single == null) {
                        single = new DCLLazySingle();
                        /*
                         *       single = new DCLLazySingle() 这不是一个原子操作,编译器可能会对指令重排序优化
                         *       1. 为new DCLLazySingle()分配内存空间
                         *       2. 执行<init>构造方法,初始化对象
                         *       3. 将single指向新创建的对象
                         */
                    }
                }
            }
            return single;
        }
    }

    //饿汉式
    public static class HungrySingle{
        private HungrySingle(){
            synchronized (HungrySingle.class){
                if (single != null) throw new RuntimeException("不要试图使用反射破坏单例");
            }
        }
        private static final HungrySingle single = new HungrySingle();
        public static HungrySingle getInstance(){
            return single;
        }
    }

    //使用静态内部类实现单例
    public static class InnerStaticSingle{
        private InnerStaticSingle(){
            synchronized (InnerStaticSingle.class){
                if (InnerStatic.single != null){
                    throw new RuntimeException("不要试图使用反射破坏单例");
                }
            }
        }
        private static class InnerStatic{
            private static final InnerStaticSingle single = new InnerStaticSingle();
        }
        public static InnerStaticSingle getInstance(){
            return InnerStatic.single;
        }
    }

    /**
     * 使用枚举类实现单例
     * enum from jdk1.5++
     * enum 的构造函数是EnumSingle.class.getDeclaredConstructor(String.class,int.class);
     * 枚举自带单例属性并且不可以反射创建实例
     * @see java.lang.reflect.Constructor#newInstance(Object...)
     *  <code>
     *      if ((clazz.getModifiers() & Modifier.ENUM) != 0)
     *          throw new IllegalArgumentException("Cannot reflectively create enum objects");
     *  </code>
     */
    public enum EnumSingle{
        SINGLE;
        EnumSingle getInstance(){
            return SINGLE;
        }
    }
}

