package jvm.customClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 线程上下文类加载器
 * 产生背景：
 *      需要保证接口类和具体实现类必须是由同一个类加载器加载才不会导致找不到实现类的情况
 *      API() + SPI(service provide interface) 的应用越来越多
 *      例如： java.sql.drive api接口 和 jdbc drive 实现类
 *      java.sql.drive 由ext|boot类加载器进行加载，而jdbc却是由ext|app类加载器进行加载，就有可能导致java.sql.drive找不到具体的实现类
 *
 * 线程类加载器作用： 抛弃双亲委派机制加载链模式
 *      每个线程都有一个关联的上下文类加载器，如果你使用了new Thread()方式生成新的线程，新线程会继承父线程的上下文类加载器，如果程序没有改动过上下文类加载器
 *      那么程序中所有的线程都是使用 系统类加载器 作为上下文类加载器
 *
 *      Thread.currentThread().getContextClassLoader()
 *      Thread.currentThread().setContextClassLoader()
 */
public class ContextClassLoader extends ClassLoader{

    private String rootDir;

    public ContextClassLoader (String rootDir){
        this.rootDir = rootDir;
    }


    @Override
    //重写应用类加载器的findClass方法
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = null;

        //先找是否加载过这个类，加载过直接返回
        aClass = super.findLoadedClass(name);
        if (aClass != null) return aClass;

        //自己先尝试加载，不遵守双亲委托机制
        byte[] bytes = getClassBytes(name);
        aClass = this.defineClass(name,bytes,0,bytes.length);
        if (aClass != null) return aClass;

        //自己加载不了再交给父类
        aClass = this.getParent().loadClass(name);
        return aClass;
    }

    //获取一个类字节数组
    private byte[] getClassBytes(String name) throws ClassNotFoundException {
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        try {
            is = new FileInputStream(rootDir + "/" + name.replace('.','/')+".class");

            while ((len = is.read(bytes))!=-1){
                baos.write(bytes,0,len);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }
}
