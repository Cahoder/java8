package io.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 支持异步IO的服务器端实例
 */
public class ServerSocket {

    public static void main(String[] args) throws Exception {

        /*
         * 1. 这个线程池是用来得到操作系统的“IO事件通知”的，不是用来进行“得到IO数据后的业务处理的”
         *    要进行后者的操作，您可以再使用一个池(最好不要混用)
         * 2. 您也可以不使用线程池(不推荐)，直接AsynchronousServerSocketChannel.open()即可
         */
        ExecutorService ioNotifyPool = Executors.newFixedThreadPool(20);
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(ioNotifyPool);
        AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open(group);

        //设置要监听的端口“0.0.0.0”代表本机所有IP设备
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888));
        //注意只是为AsynchronousServerSocketChannel通道注册监听
        //并不包括随后为客户端和服务器SocketChannel通道注册的监听
        serverSocket.accept(null, new ServerSocketChannelHandle(serverSocket));

        synchronized (ServerSocket.class) {
            ServerSocket.class.wait();
        }
    }

}
