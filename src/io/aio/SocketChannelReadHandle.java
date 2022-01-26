package io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * 负责对每一个socketChannel的数据获取事件进行监听。<p>
 *
 * 重要的说明: 一个socketChannel都会有一个独立工作的SocketChannelReadHandle对象(CompletionHandler接口的实现)，
 * 其中又都将独享一个“文件状态标示”对象FileDescriptor、
 * 一个独立的由程序员定义的Buffer缓存(这里我们使用的是ByteBuffer)、
 * 所以不用担心在服务器端会出现“窜对象”这种情况，因为JAVA AIO框架已经帮您组织好了。<p>
 *
 * 但是最重要的，用于生成channel的对象: AsynchronousChannelProvider是单例模式，无论在哪组socketChannel，
 * 对是一个对象引用(但这没关系，因为您不会直接操作这个AsynchronousChannelProvider对象)。
 */
public class SocketChannelReadHandle implements CompletionHandler<Integer, StringBuffer> {

    private final AsynchronousSocketChannel socketChannel;

    /**
     * 专门用于进行这个通道数据缓存操作的ByteBuffer<br>
     * 当然，您也可以作为CompletionHandler的attachment形式传入。<br>
     * 在这段示例代码中，attachment被我们用来记录所有传送过来的StringBuffer了。
     */
    private final ByteBuffer byteBuffer;

    public SocketChannelReadHandle(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
        this.socketChannel = socketChannel;
        this.byteBuffer = byteBuffer;
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
     */
    @Override
    public void completed(Integer result, StringBuffer attachment) {
        //如果条件成立，说明客户端主动终止了TCP套接字，这时服务端终止就可以了
        if(result == -1) {
            closeSocket();
            return;
        }

        System.out.println("this("+this+").completed(" +
                "Integer result("+result+"), StringBuffer attachment("+attachment+")" +
                ") : 然后我们来取出通道中准备好的值");
        /*
         * 实际上，由于我们从Integer result知道了本次channel从操作系统获取数据总长度
         * 所以实际上，我们不需要切换成“读模式”的，但是为了保证编码的规范性，还是建议进行切换。
         *
         * 另外，无论是JAVA AIO框架还是JAVA NIO框架，都会出现“buffer的总容量”小于“当前从操作系统获取到的总数据量”，
         * 但区别是，JAVA AIO框架中，我们不需要专门考虑处理这样的情况，因为JAVA AIO框架已经帮我们做了处理(做成了多次通知)
         * */
        this.byteBuffer.flip();
        byte[] contexts = new byte[1024];
        this.byteBuffer.get(contexts, 0, result);
        this.byteBuffer.clear();
        String nowContent = new String(contexts , 0 , result , StandardCharsets.UTF_8);
        attachment.append(nowContent);
        System.out.println("================目前的传输结果: " + attachment);

        //如果条件成立，说明还没有接收到“结束标记”
        if(attachment.indexOf("over") == -1) {
            //还要继续监听(超出buffer容量会做成多次监听)
            this.socketChannel.read(this.byteBuffer, attachment, this);
            return;
        }

        //=========================================================================
        //          这里以“over”符号作为客户端业务信息完整的标记
        //=========================================================================
        System.out.println("=======收到完整信息，开始处理业务=========");

        //服务端响应客户端,可以改进通过创建类似SocketChannelWriteHandle实现类
        ByteBuffer writeBuffer = ByteBuffer.wrap("OK".getBytes(StandardCharsets.UTF_8));
        this.socketChannel.write(writeBuffer);

        closeSocket();
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
     */
    @Override
    public void failed(Throwable exc, StringBuffer attachment) {
        System.out.println("=====发现客户端异常关闭，服务器将关闭TCP通道");
        closeSocket();
    }

    private void closeSocket() {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
