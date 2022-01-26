package io.nio;

import java.io.OutputStream;
import java.net.Socket;

public class SocketClientDaemon {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8888);
        OutputStream out = socket.getOutputStream();
        String s = "hello world (over)";
        out.write(s.getBytes());
        out.close();

        synchronized (SocketClientDaemon.class) {
            SocketClientDaemon.class.wait();
        }
    }
}
