package io.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端Reactor模型
 * Reactor角色实现在一个多路复用器上等待客户端连接
 * 将其分发给Acceptor专门进行客户端连接的获取
 */
public class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        this.selector = Selector.open();  // 创建一个Selector多路复用器
        this.serverSocket = ServerSocketChannel.open();  // 创建服务端的ServerSocketChannel
        this.serverSocket.configureBlocking(false);  // 设置为非阻塞模式
        SelectionKey key = this.serverSocket.register(this.selector, SelectionKey.OP_ACCEPT);
        this.serverSocket.bind(new InetSocketAddress(port));  // 绑定服务端端口
        key.attach(new Acceptor(this.serverSocket));  // 为服务端Channel绑定一个Acceptor
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select(); //服务端使用一个线程不断等待客户端的连接到达
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());  // 监听到客户端连接事件后将其分发给Acceptor
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        // 这里的attachment也即前面为服务端Channel绑定的Acceptor，
        // 调用其run()方法进行客户端连接的获取，并且进行分发
        Acceptor attachment = (Acceptor) key.attachment();
        attachment.run();
    }
}
