package jvm.customClassLoader;

import java.io.*;

/**
 * 自定义加密类字节码文件
 */
public class EncryptClassUtil {

    public void Encrypt(String src,String dest){
        File from = new File(src);
        File to = new File(dest);
        if (!from.exists() || !from.isFile() || !src.endsWith(".class")) throw  new IllegalArgumentException("类文件不存在!");
//        if (to.isFile()) throw  new IllegalArgumentException("输出文件已存在!");
        if (to.exists() && to.isDirectory()) throw  new IllegalArgumentException("请填写完整的输出文件名!");

        try {
            FileInputStream fis = new FileInputStream(from);
            FileOutputStream fos = new FileOutputStream(to);

            ByteInvert(fis,fos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ByteInvert(InputStream in,OutputStream out) throws IOException {
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
