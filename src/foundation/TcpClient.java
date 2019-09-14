package foundation;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.lang.String;

//socket 客户端
public class TcpClient {

    private Socket client;            //客户端
    private OutputStream os = null;   //发送流
    private InputStream is = null;    //接收流

    public TcpClient(String host, int port) throws IOException{
        try {
            this.client = new Socket(host,port);  //服务器端必须先启动，否则连接失败 throws ConnectException
            this.os = client.getOutputStream();  //必须使用Socket提供的流对象给服务器发送数据
            this.is = client.getInputStream();  //必须使用Socket提供的流对象接收服务器的数据

            System.out.println( Thread.currentThread().getName() + "启动：" + client.toString());
        }catch (ConnectException connect){
            client = null;
            System.out.println("请求服务端失败： " + connect.getMessage());
        }
    }

    //客户端发送文件
    public void request(File file) throws IOException {
        if (client==null)return;
        if (!file.exists()){
            System.out.println("上传失败，该文件不存在！");
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] data = new byte[1024];
        int len;
        while ((len = bis.read(data))!= -1){   //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            os.write(data,0,len);
        }
        os.flush();

        client.shutdownOutput();    //对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。（类似于给输出流添加一个结合标记-1，不加的话while读取不到-1进入死循环堵塞状态）
        receive();
    }

    //客户端发送要下载的文件名
    public void request(String savepath , String filename) throws IOException {
        if (client==null)return;
        File dir = new File(savepath);
        if (!dir.isDirectory()){
            System.out.println("下载失败：储存路径不存在，请先创建相应的文件夹！");
            return;
        }
        File file = new File(savepath+"/"+filename);
        if (file.exists()){
            System.out.println("下载失败：该文件已经存在！");
            return;
        }

        if (client.isConnected()){
            os.write(filename.getBytes());
            System.out.println(Thread.currentThread().getName() +"发送要下载的文件名： " + filename);
        }
        os.flush();

        client.shutdownOutput();    //对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。（类似于给输出流添加一个结合标记-1，不加的话while读取不到-1进入死循环堵塞状态）
        receive(file);
    }

    //客户端发送数据
    public void request(String message) throws IOException {
        if (client==null)return;
        if (client.isConnected()){
            os.write(message.getBytes());
            System.out.println(Thread.currentThread().getName() +"发送消息： " + message);
        }
        os.flush();

        client.shutdownOutput();    //对于 TCP 套接字，任何以前写入的数据都将被发送，并且后跟 TCP 的正常连接终止序列。（类似于给输出流添加一个结合标记-1，不加的话while读取不到-1进入死循环堵塞状态）
        receive();
    }

    //客户端接收数据
    private void receive() throws IOException{
        byte[] data = new byte[1024];
        int len;
        while ((len = is.read(data))!= -1){   //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            System.out.println( Thread.currentThread().getName() + "接收消息： " + new String(data,0,len));
        }
    }

    //客户端接收下载的文件
    private void receive(File filename) throws IOException{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
        byte[] data = new byte[1024];
        int len;
        while ((len = is.read(data))!= -1){   //如果 len 不为 0，则在输入可用之前，该方法将阻塞；
            bos.write(data,0,len);
        }
        bos.close();

        /*TODO if download failed !*/



    }

    //关闭客户端
    public void close() throws IOException{
        if (client==null)return;
        client.close();
        System.out.println("------"+Thread.currentThread().getName()+"关闭了连接！"+"------");
    }
}
