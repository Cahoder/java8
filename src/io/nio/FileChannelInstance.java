package io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * NIO文件通道实例
 * @author caihd
 * @date 2022-1-21
 */
public class FileChannelInstance {

    public static void fileCopy(String src, String dist) throws IOException {

        /* 获得源文件的输入字节流 */
        FileInputStream fin = new FileInputStream(src);

        /* 获取输入字节流的文件通道 */
        FileChannel fcin = fin.getChannel();

        /* 获取目标文件的输出字节流 */
        FileOutputStream fout = new FileOutputStream(dist);

        /* 获取输出字节流的通道 */
        FileChannel fcout = fout.getChannel();

        /* 为缓冲区分配 1024 个字节 */
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while (true) {

            /* 从输入通道中读取数据到缓冲区中 */
            int r = fcin.read(buffer);

            /* read() 返回 -1 表示 EOF */
            if (r == -1) {
                break;
            }

            /* 切换读写 */
            buffer.flip();

            /* 把缓冲区的内容写入输出文件中 */
            fcout.write(buffer);

            /* 清空缓冲区 */
            buffer.clear();
        }
    }

    public static void transferTo(String src, String dist) throws IOException {
        FileChannel fromChannel = new RandomAccessFile(src, "rw").getChannel();
        FileChannel toChannel = new RandomAccessFile(dist, "rw").getChannel();
        long position = 0L;
        long offset = fromChannel.size();
        fromChannel.transferTo(position, offset, toChannel);
    }

    public static void transferFrom(String dist, String src) throws IOException {
        FileChannel fromChannel = new RandomAccessFile(dist, "rw").getChannel();
        FileChannel toChannel = new RandomAccessFile(src, "rw").getChannel();
        long position = 0L;
        long offset = fromChannel.size();
        toChannel.transferFrom(fromChannel, position, offset);
    }

    public static void writeToFileByMappedByteBuffer(String content, String dist) throws IOException {
        Path path = Paths.get(dist);
        File file = path.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("Destination Path File Doesn't Exist !");
        }
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
        if (mappedByteBuffer != null) {
            mappedByteBuffer.put(bytes);
            /* 把缓冲区更改的内容强制写入本地文件 */
            mappedByteBuffer.force();
        }
    }

    public static String readFromFileByMappedByteBuffer(String src, int length) throws IOException {
        Path path = Paths.get(src);
        File file = path.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException("Source Path File Doesn't Exist !");
        }
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
        if (mappedByteBuffer != null) {
            byte[] bytes = new byte[length];
            mappedByteBuffer.get(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("Can Not Read From Source Path File !");
    }

}
