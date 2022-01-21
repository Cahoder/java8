package io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author caihd
 * @date 2022-1-21
 */
public class SocketClientThread implements Runnable {

    private final CountDownLatch latch;

    private final Integer clientIdx;

    public SocketClientThread(CountDownLatch latch, Integer clientIdx) {
        this.latch = latch;
        this.clientIdx = clientIdx;
    }

    @Override
    public void run() {
        Socket socket;
        OutputStream clientRequest = null;
        InputStream clientResponse = null;

        try {

            socket = new Socket("127.0.0.1", 83);
            clientRequest = socket.getOutputStream();
            clientResponse = socket.getInputStream();

            //等待直到SocketClientDaemon完成所有线程的启动，然后所有线程一起发送请求
            this.latch.await();

            //发送请求信息
            clientRequest.write(("这是第 " + this.clientIdx + " 个客户端的请求。").getBytes());
            clientRequest.flush();

            //监听返回信息
            int maxLen = 1024;
            byte[] contextBytes = new byte[maxLen];
            int realLen;
            StringBuilder message = new StringBuilder();
            //程序执行到这里，会一直等待服务器返回信息(注意，前提是in和out都不能close，如果close了就收不到服务器的反馈了)
            while((realLen = clientResponse.read(contextBytes, 0, maxLen)) != -1) {
                message.append(new String(contextBytes, 0, realLen));
            }
            System.out.println("接收到来自服务器的信息:" + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientRequest != null) {
                    clientRequest.close();
                }
                if (clientResponse != null) {
                    clientResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
