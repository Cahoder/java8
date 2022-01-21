package io.nio;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * 通过NIO选择器/渠道
 * 实现套接字服务端
 * @author caihd
 * @date 2022-1-21
 */
public class SocketServerDaemon {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

    }

}
