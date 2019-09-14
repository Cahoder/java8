package apiSuper;

import foundation.TcpClient;
import foundation.TcpService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.String;

/**
 * 网络编程：
 *      socket套接字：包含了IP地址和端口号的网络单位，是两台机器间通信的端点
 *
 *      注：
 *          客户端和服务器端进行交互必须使用Socket中提供的网络流，不能够自己创建网络流Stream对象
 */
public class SocketClass {

    // 模拟 B/S 通信过程
    public void BrowserServiceSocket(){
        try {
            ServerSocket server = new ServerSocket(8080);
            while (true){
                Socket socket = server.accept();
                //浏览器每请求一个文件就开启一条线程去处理请求
                new Thread(()->{
                    try {
                        InputStream requestStream = socket.getInputStream();

                        //首先先用缓冲数组字符流读取 浏览器传过来的 HTTP 请求头信息
                        BufferedReader requestHead = new BufferedReader(new InputStreamReader(requestStream));
//                        int len;
//                        char[] chars = new char[1024];
//                        while ((len = requestHead.read(chars))!=-1){
//                            System.out.println(new String(chars,0,len));
//                        }

                        //获取浏览器想要请求的文件地址
                        String requestFile = requestHead.readLine().split(" ")[1].substring(1);
                        //使用网站的资源根目录拼接请求的文件地址
                        String webSiteRoot = "./";
                        File file = new File(webSiteRoot+requestFile);
                        if (!file.exists()) throw new IOException("请求的资源文件不存在！");

                        //给浏览器回传请求的资源
                        OutputStream respond = socket.getOutputStream();

                        //1.先写入HTTP协议的响应头信息
                        respond.write("HTTP/1.1 200 OK\r\n".getBytes());
                        respond.write("Content-Type:text/html\r\n".getBytes());
                        //必须再写入空行，否则浏览器不解析
                        respond.write("\r\n".getBytes());

                        //2.回写请求的本地的资源
                        FileInputStream fis = new FileInputStream(file);
                        int len;
                        byte[] bytes = new byte[1024];
                        while ((len = fis.read(bytes))!=-1){
                            respond.write(bytes,0,len);
                        }

                        //释放资源
                        fis.close();
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //客户端和服务端的通信过程
    public void HelloSocket(){
        //先启动服务器端
        Runnable s = () ->{
            try {
                TcpService service = TcpService.sendMessage(8080);

            }catch (IOException e){
                e.printStackTrace();
            }
        };
        new Thread(s,"服务器端").start();

        //启动客户端去访问
        Runnable c = () ->{
            try {
                Thread.sleep(2000);
                TcpClient client = new TcpClient("127.0.0.1", 8080);
                client.request("你好啊服务器！");
                client.close();

            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        };
        new Thread(c,"客户端A").start();
    }


    //文件上传
    public void uploadFileSocket(){
        //先启动服务器端
        Runnable s = () ->{
            try {
                TcpService service = TcpService.uploadFile(8080,"jpg");

            }catch (IOException e){
                e.printStackTrace();
            }
        };
        new Thread(s,"服务器端").start();
        //启动客户端去访问
        Runnable c = () ->{
            try {
                Thread.sleep(3000);
                TcpClient client = new TcpClient("127.0.0.1", 8080);
                File file = new File("/home/cahoder/Pictures/Autumn_in_Kanas_by_Wang_Jinyu.jpg");
                client.request(file);
                client.close();
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        };
        new Thread(c,"客户端A").start();
    }


    //文件下载
    public void downloadFileSocket(){
        //先启动服务器端
        Runnable s = () ->{
            try {
                TcpService service = TcpService.downloadFile(8080);
            }catch (IOException e){
                e.printStackTrace();
            }
        };
        new Thread(s,"服务器端").start();
        //启动客户端去访问
        Runnable c = () ->{
            try {
                Thread.sleep(3000);
                TcpClient client = new TcpClient("127.0.0.1", 8080);

                client.request("./Download","156490577351575844.jpg");
                client.close();
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        };
        new Thread(c,"客户端A").start();
    }
}
