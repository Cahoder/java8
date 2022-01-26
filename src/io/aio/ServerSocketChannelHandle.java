package io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 负责响应ServerSocketChannel事件
 * 同时向操作系统注册IO监听完成后事件
 */
public class ServerSocketChannelHandle implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private final AsynchronousServerSocketChannel serverSocketChannel;

    public ServerSocketChannelHandle(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * 注意分别观察 this socketChannel attachment三个对象的内存地址
     * 在不同客户端连接到达时这三个对象的变化,以说明ServerSocketChannelHandler的监听模式
     */
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        System.out.println("this("+this+").completed(" +
                "AsynchronousSocketChannel socketChannel("+socketChannel+"), Void attachment("+attachment+")" +
                ")");
        //每次都要重新注册监听(一次注册，一次响应)，但是由于“文件状态标示符”是独享的，所以不需要担心有“漏掉的”事件
        this.serverSocketChannel.accept(attachment, this);

        //为这个新的socketChannel注册“read”事件，以便操作系统在收到数据并准备好后，主动通知应用程序
        //由于我们要将客户端多次传输的数据累加起来一起处理，所以将stringBuffer作为一个“附件”依附在这个channel上
        ByteBuffer readBuffer = ByteBuffer.allocate(50);
        socketChannel.read(readBuffer, new StringBuffer(), new SocketChannelReadHandle(socketChannel, readBuffer));
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
     */
    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("failed(Throwable exc, ByteBuffer attachment)");
    }

}
