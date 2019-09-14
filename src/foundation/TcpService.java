package foundation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.String;
import java.util.Random;

//socket 服务端
public class TcpService{

    private ServerSocket service;    //服务端
    private Socket client;           //用于接收当前请求的客户端对象
    private byte[] data = new byte[1024];  //缓存数据

    private TcpService(int port) throws IOException {
        service = new ServerSocket(port);  //服务器端必须先启动
        System.out.println( Thread.currentThread().getName() + "已启动：" + service.toString());
    }

    //接收消息
    public static TcpService sendMessage(int port) throws IOException{
        return new TcpService(port).monitor();
    }

    //接收上传文件
    public static TcpService uploadFile(int port, String fileType) throws IOException{
        String filename = ""+  System.currentTimeMillis() + new Random().nextInt(99999);
        return new TcpService(port).monitor(new File("./upload",filename+"."+fileType.toLowerCase()));
    }

    //返回下载文件
    public static TcpService downloadFile(int port) throws IOException{
        return new TcpService(port).monitor("./upload");
    }

    //默认监听客户端
    private TcpService monitor() throws IOException {
        while (Thread.currentThread().getState() == Thread.State.RUNNABLE && !service.isClosed()){
            if (client == null) {
                /*TODO sleep service if none client connect here !*/
                //wait coding ......!


                //会阻塞线程一直等待新的客户端的连接！！！
                client = service.accept();
            }
            receive();
            request("你好啊客户端！端口："+client.getPort());
            detectClient();
        }
        return this;
    }


    /**
     * 监听上传文件
     * @param file  save the file to where
     * @return TcpService
     */
    private TcpService monitor(File file) throws IOException {
        while (Thread.currentThread().getState() == Thread.State.RUNNABLE && !service.isClosed()){
            if (client == null) {
                /*TODO sleep service if none client connect here !*/
                //wait coding ......!


                //会阻塞线程一直等待新的客户端的连接！！！
                client = service.accept();
            }
            new Thread(()->{
                try {
                    receive(file);
                    request("上传成功！");
                }catch (IOException | IllegalArgumentException e){
                    try {
                        request("上传失败: " + e.getMessage());
                    } catch (IOException ignored) {}
                }
            }).start();
            detectClient();
        }
        return this;
    }

    /**
     * 监听下载文件
     * @param fileDir where is the file save at
     * @throws IOException while download failed
     */
    private TcpService monitor(String fileDir) throws IOException {
        while (Thread.currentThread().getState() == Thread.State.RUNNABLE && !service.isClosed()){
            if (client == null) {
                /*TODO sleep service if none client connect here !*/
                //wait coding ......!


                //会阻塞线程一直等待新的客户端的连接！！！
                client = service.accept();
            }
            new Thread(()->{
                try {
                    receive(fileDir);
                }catch (IOException | IllegalArgumentException e){
                    try {
                        request("下载失败: " + e.getMessage());
                    } catch (IOException ignored) {}

                }
            }).start();

            detectClient();
        }
        return this;
    }


    //接收客户端传来的字节流
    private void receive() throws IOException {
        //接收流
        InputStream is = client.getInputStream();
        int len;
        while ((len = is.read(data))!= -1) {    //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            System.out.println("服务端接收消息： " + new String(data, 0, len));
        }
    }

    //接收客户端传来的文件
    private void receive(File file) throws IOException {
        File dir = new File(file.getParent());
        boolean mkdirs = dir.mkdirs();
        if (!dir.exists() && !mkdirs) throw new IllegalArgumentException("服务器出现问题！");
        if (file.exists()) throw new IllegalArgumentException("该文件已存在，请重试！");
        //接收流
        InputStream is = client.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);   //服务器本地输出流
        int len;
        while ((len = is.read(data))!= -1) {    //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            fos.write(data,0,len);
        }
        fos.close();
    }

    //接收客户端想要下载的文件
    private void receive(String fileDir) throws IOException {
        //接收流
        InputStream is = client.getInputStream();
        int len;
        StringBuilder filename = new StringBuilder();
        while ((len = is.read(data))!= -1) {    //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            filename.append(new String(data,0,len));
        }

        if (filename.toString().equals("")) return;
        File file = new File(fileDir + "/" + filename.toString());
        request(file);
    }

    //回复文字
    private void request( String message ) throws IOException {
        //发送流
        OutputStream os = client.getOutputStream();
        os.write(message.getBytes());
        System.out.println("服务端发送消息： " + message);
        os.flush();
        client.shutdownOutput();   //对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。（类似于给输出流添加一个结合标记-1，不加的话while读取不到-1进入死循环堵塞状态）
    }

    //回复文件
    private void request(File file) throws  IOException{
        //发送流
        OutputStream os = client.getOutputStream();
        if (!file.exists()){
            throw new IllegalArgumentException("该下载文件不存在服务器上！");
        }else {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int len;
            while ((len = bis.read(data))!= -1) {    //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
                os.write(data,0,len);
            }
            os.flush();
            client.shutdownOutput();   //对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。（类似于给输出流添加一个结合标记-1，不加的话while读取不到-1进入死循环堵塞状态）
        }
    }

    //判断当前客户端的连接状态(服务器发送3次嗅探包)
    //使用isConnected()|isClosed() 方法检测客户端是否还连接不准确，那个只能检测到客户端socket在内存中的状态
    private void detectClient() throws IOException {
        int detectTime = 3;
        while (detectTime>0){
            try {
                if (client!=null) client.sendUrgentData(0xFF);  //发送紧急包嗅探
                detectTime--;
                Thread.sleep(1000);
            }
            catch (IOException e) {
                disconnect();
                break;
            } catch (InterruptedException e){e.printStackTrace();}
        }
    }

    //关闭与当前客户端的连接
    private void disconnect() throws IOException{
        System.out.println("---端口号"+client.getPort()+"客户端关闭了连接，服务器也关闭与它的连接！---");
        client.close();
        client = null;
    }

    //关闭服务端,只能类内部关闭服务，例如内部出现什么错误时候关闭
    private void close() throws IOException{
        service.close();   //如果线程在accept()阻塞状态下关闭会抛出异常
        System.out.println("服务端停止服务！");
    }
}
