package io.bio;

import java.util.concurrent.CountDownLatch;

/**
 * 并发客户端请求
 * @author caihd
 * @date 2022-1-21
 */
public class SocketClientDaemon {

    public static void main(String[] args) throws Exception {
        int clientNumber = 20;
        CountDownLatch latch = new CountDownLatch(clientNumber);

        for (int idx = 0; idx < clientNumber; idx++, latch.countDown()) {
            SocketClientThread clientThread = new SocketClientThread(latch, idx);
            new Thread(clientThread).start();
        }

        synchronized (SocketClientDaemon.class) {
            SocketClientDaemon.class.wait();
        }
    }

}
