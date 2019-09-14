package jvm.customClassLoader;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 自定义获取来自网络的类加载器
 */
public class NetWorkClassLoader extends ClassLoader {
    private String rootUrl;

    public NetWorkClassLoader(String rootUrl){
        this.rootUrl = rootUrl;
    }


    @Override
    //重写应用类加载器的findClass方法
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = null;

        //先找是否加载过这个类，加载过直接返回
        aClass = super.findLoadedClass(name);
        if (aClass != null) return aClass;

        //遵循双亲委托机制，先让父类尝试加载，父类可以加载就返回 (当然可以不遵守双亲委托机制，自己加载不了再交给父类)
        try {
            aClass = this.getParent().loadClass(name);
            if (aClass != null) return aClass;
        } catch (ClassNotFoundException e){}

        //最后自己尝试加载
        byte[] bytes = getClassBytes(name);
        aClass = this.defineClass(name,bytes,0,bytes.length);

        return aClass;
    }

    //获取一个类字节数组
    private byte[] getClassBytes(String name) throws ClassNotFoundException {
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        try {
            is = new URL(rootUrl + "/" + name.replace('.','/')+".class").openStream();

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
