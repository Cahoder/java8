package io.nio.reactor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端Reactor模型
 * Acceptor主线程获取到客户端连接事件后
 * 通过线程池将其分发给Handler进行网络读写
 */
public class Acceptor implements Runnable {
    private final ExecutorService executor = Executors.newFixedThreadPool(20);
    private final ServerSocketChannel serverSocket;

    public Acceptor(ServerSocketChannel serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            SocketChannel channel = serverSocket.accept();  // 获取客户端连接
            if (null != channel) {
                executor.execute(new Handler(channel));  // 将客户端连接交由线程池处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
