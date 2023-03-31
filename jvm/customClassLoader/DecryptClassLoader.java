package jvm.customClassLoader;

import java.io.*;

/**
 * 自定义加密解密类加载器
 */
public class DecryptClassLoader extends ClassLoader {
    private String rootDir;

    public DecryptClassLoader(String rootDir){
        this.rootDir = rootDir;
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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            InputStream is = new FileInputStream(rootDir + "/" + name.replace('.','/')+".class");

            ByteInvert(is,baos);

        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        return baos.toByteArray();
    }


    private void ByteInvert(InputStream in, ByteArrayOutputStream out) throws IOException {
        if (in == null || out == null) throw new IOException();

        BufferedInputStream bis = new BufferedInputStream(in);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        int len;
        while ((len = bis.read())!= -1){
            bos.write(len ^ 255);   //字节按位取反
        }

        bos.close();
        bis.close();

    }
}
