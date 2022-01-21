package io.bio;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多线程服务端处理
 * @author caihd
 * @date 2022-1-21
 */
public class SocketServerDaemon {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(83)) {
            while (true) {
                Socket socket = serverSocket.accept();
                //当然业务处理过程可以交给一个线程(这里可以使用线程池),并且线程的创建是很耗资源的。
                //最终改变不了accept()只能一个一个接受socket的情况,并且被阻塞的情况
                SocketServerThread socketServerThread = new SocketServerThread(socket);
                new Thread(socketServerThread).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
