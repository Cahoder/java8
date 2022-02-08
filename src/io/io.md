### IO基本概念

- 阻塞和非阻塞（程序级别）

  指程序请求OS的IO操作，如果IO资源还未就绪，程序应该如何处理的问题；

  前者等待，后者继续执行(使用线程一直轮询)

- 同步和非同步（操作系统级别）

  指OS收到程序的IO请求，如果IO资源还未就绪，OS如何响应程序的问题；

  前者不响应直到IO资源就绪，后者先返回一个标记(后续利用事件机制通知)

- IO交互经历的两个阶段

  1. 等待数据准备好（复制到内核中某缓冲区）
  2. 从内核（缓冲区）向进程复制数据

### IO内核实现形式

- select、poll、epoll、kqueue

  - select轮询

    timeout参数精度为1ns（poll和epoll为1ms），适用于实时性要求更高的场景，select平台支持性和移植性更好

    存在最大文件描述符（fd）数量限制（Linux默认1024）

  - poll轮询

    无最大文件描述符（fd）数量限制

    如果需监控的 fd < 1000，没有必要使用epoll（发挥不出优势）

    如果需监控的fd变化多且短暂，没有必要使用epoll（epoll的fd放在内核中，会造成频繁系统调用，且不易调试）

  - epoll事件驱动

    适用场景：大量的idle-connection或者dead-connection（长连接状态）

    优势：无最大文件描述符（fd）数量限制，且IO效率不随fd数量增长而线性下降

    实现：创建一个文件描述符来管理多个描述符，一旦基于某个fd就绪时，

    内核会采用类似callback的机制迅速激活此fd，当进程调用epoll_wait()时便得到通知

    > **int epoll_create(int size)**  //创建一个epoll的句柄，size用来告诉内核这个监听的数目

    > **int epoll_ctl(int epfd, int op, int fd, struct epoll_event \* event)**
    >
    > - epfd : epoll_create() 返回值
    > - op : 三个宏可表示 ( 添加EPOLL_CTL_ADD、删除EPOLL_CTL_DEL、修改EPOLL_CTL_MOD )
    > - fd : 内核需要监听的文件描述符
    > - epoll_event : 内核需要监听的事件

    > **int epoll_wait(int epfd, struct epoll_event \* events, int maxevents, int timeout)**
    >
    > 等待epfd上返回的io事件，最多返回maxevents个事件
    >
    > 该函数返回需要处理的事件数目，如返回0表示已超时
    >
    > - events : 从内核得到事件的集合
    > - maxevents : 告知events集合有多大，不超过epoll_create()时的size
    > - timeout（ms）: 0立即返回    -1不确定

- epoll工作模式

  - LT（Level Trigger）模式

    当 epoll_wait() 检测到描述符事件到达时，将此事件通知进程，进程可以不立即处理该事件，下次调用 epoll_wait() 会再次通知进程。是默认的一种模式，并且同时支持 Blocking 和 No-Blocking。

  - ET（Edge Trigger）模式

    通知后进程必须立即处理事件，下次再调用 epoll_wait() 时不会再得到事件到达通知。减少了 epoll 事件被重复触发次数，因此效率要比 LT 模式高。只支持 No-Blocking，以避免由于一个文件句柄的阻塞读/阻塞写操作把处理多个文件描述符的任务饿死。

### Unix 5种IO模型

- 阻塞式 IO - BIO

  > 阻塞当前应用进程，但不影响其他应用程序执行（不消耗CPU时间）

  ![阻塞式IO模型](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-model-0.png)

- 非阻塞式 IO - NIO

  > 不阻塞当前应用，内核先返回错误码。轮询系统调用判断IO完成状态（耗费CPU时间）

  ![阻塞式 IO模型](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-model-1.png)

- IO复用/事件驱动IO (select 和 poll)

  > 阻塞单个进程等待多个套接字中任一变为可读，阻塞并执行IO系统调用方法复制数据

  ![IO复用模型](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-model-2.png)

- 信号驱动式IO (SIGIO)

  > 应用进程使用sigaction系统调用。内核立即返回不阻塞，待数据到达时向应用进程发送SIGIO信号

  ![信号驱动式IO模型](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-model-3.png)

- 异步IO - AIO

  > 差异点：异步IO的信号是通知应用进程IO完成，而信号驱动IO的信号是通知应用进程可以开始IO

  ![异步IO模型](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-model-4.png)

### 五种IO模型比较

![五大IO模型比较](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/pics/1492928105791_3.png)

### BIO通信方式

- 并发请求串行化，无法满足业务场景
- 服务端多线程优化的局限性（伪异步方式）
  1. 业务处理虽然交给独立线程，但OS通知accept()方法还是串行
  2. 操作系统可创建的线程有限
  3. 每个线程jvm都要分配堆栈空间，即使ThreadPoolExecutor线程池一样会引发BlockingQueue积压
  4. 如果应用为长连接，线程不能及时关闭会导致系统资源的消耗更失控

### NIO通信方式

- NIO以多字节块的方式处理数据（IO以单字节流的方式）

- Channel是对原IO流的模拟（流是单工方式，通道是全双工）

- Reactor模型（让单个线程使用一个选择器轮询监听多个通道）

  <!--需配置监听的通道 Channel 为非阻塞-->

  ![选择器模型](https://pdai.tech/_images/pics/4d930e22-f493-49ae-8dff-ea21cd6895dc.png)

### Java对多路复用IO的支持

![Java多路复用IO](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-nio-1.png)

- Channel

  - FileChannel : 从文件中读写数据

  - DatagramChannel : 通过 UDP 读写网络中数据

  - SocketChannel : 通过 TCP 读写网络中数据

  - ServerSocketChannel : 服务端监听 TCP/UDP 协议数据

- Buffer是通道数据读写必经之路（缓冲区提供了对数据的结构化访问/跟踪系统的读写进程）

  - 支持的类型：ByteBuffer CharBuffer ShortBuffer IntBuffer LongBuffer FloatBuffer DoubleBuffer
  - 缓冲区状态变量：capacity(最大容量)  position(已读写字节数)  limit(剩余可读写字节数)

- Selector

  - 事件订阅和Channel管理

    ```java
    //将通道注册到选择器上，需指定要注册的具体事件
    public class SelectionKey {
        public static final int OP_READ = 1 << 0;
        public static final int OP_WRITE = 1 << 2;
        public static final int OP_CONNECT = 1 << 3;
        public static final int OP_ACCEPT = 1 << 4;
    }
    ```

  - 轮询代理 - 应用层不再通过阻塞模式或者非阻塞模式直接询问操作系统“事件有没有发生”，而是由Selector代其询问。

  - jvm根据不同操作系统实现xxxChannelImpl支持

    ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-nio-5.png)

### NIO零拷贝实现

- Java NIO框架对零拷贝的几种实现

  Java NIO中的**Channel**相当于OS中Kernel Space的缓冲区，而**Buffer**对应OS中User Space的缓冲区

  - 通道是全双工（双向传输），即可能是读缓冲区，也可能是网络缓冲区

  - 缓冲区分为堆内存（HeapBuffer）和堆外内存（DirectBuffer，通过malloc()分配的用户态内存）

  HeapBuffer中数据会面临被JVM GC的风险，因此NIO会先调用`sun.misc.Unsafe.copyMemory()`把HeapBuffer中数据拷贝到临时的DirectBuffer中的本地内存(native memory)，背后原理与memcpy()类似，所以注意DirectBuffer需要应用程序手动释放。最后将拷贝到DirectBuffer中的数据内存地址返给IO调用函数，避免再次访问Java对象处理IO读写。
  
  <!--所以我们是否能够提供一个目标文件作为本地内存地址映射达到零拷贝目标？-->
  
  > **MappedByteBuffer（基于mmap内存映射方式）**
  >
  > - 它继承自ByteBuffer，FileChannel中定义了一个map抽象方法（`sun.nio.ch.FileChannelImpl.java` 类负责实现）
  >
  >   ```java
  >   public abstract MappedByteBuffer map(MapMode mode, long position, long size)
  >       throws IOException;
  >   //mode：限定内存映射区域（MappedByteBuffer）对内存映像文件的访问模式，包括只可读（READ_ONLY）、可读可写（READ_WRITE）和写时拷贝（PRIVATE）三种模式。
  >   //position：文件映射的起始地址，对应内存映射区域（MappedByteBuffer）的首地址。
  >   //size：文件映射的字节长度，从 position 往后的字节数，对应内存映射区域（MappedByteBuffer）的大小。
  >
  >   该抽象方法的实现类通过调用map0方法获取指定内存的数据
  >   private native long map0(int prot, long position, long mapSize) 
  >       throws IOException;
  >   map0底层通过调用sun.misc.Unsafe类中的getByte()和putByte()方法读写数据替代了IO对象中的read()和write()方法
  >   ```
  >
  > - MappedByteBuffer 相比 ByteBuffer 新增了 fore()、load() 和 isLoad() 三个重要的方法
  >
  >   ```java
  >   //fore()：对于处于 READ_WRITE 模式下的缓冲区，把对缓冲区内容的修改强制刷新到本地文件。 //load()：将缓冲区的内容载入物理内存中，并返回这个缓冲区的引用。
  >   //isLoaded()：如果缓冲区的内容在物理内存中，则返回 true，否则返回 false。
  >   ```
  >
  > - MappedByteBuffer 使用堆外的虚拟内存,因此不受JVM的 -Xmx 参数限制，但map文件的大小超过Integer.MAX_VALUE 字节，需要通过 position 参数重新 map 文件后面的内容
  >
  > - MappedByteBuffer 在处理大文件时性能的确很高，但也存内存占用、文件关闭不确定等问题，被其打开的文件只有在垃圾回收的才会被关闭，而且这个时间点是不确定的
  >
  > - MappedByteBuffer 提供了文件映射内存的 mmap() 方法，也提供了释放映射内存的 unmap() 方法。然而 unmap() 是 FileChannelImpl 中的私有方法，无法直接显示调用。因此用户程序需要通过 Java 反射的调用 sun.misc.Cleaner 类的 clean() 方法手动释放映射占用的内存区域
  >
  >   ```java
  >   public static void clean(final Object buffer) throws Exception {
  >       AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
  >           try {
  >               Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
  >               getCleanerMethod.setAccessible(true);
  >               Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
  >               cleaner.clean();
  >           } catch(Exception e) {
  >               e.printStackTrace();
  >           }
  >       });
  >   }
  >   ```
  
  > **DirectByteBuffer（直接使用堆外用户态内存方式）**
  >
  > - 一般通过ByteBuffer类中静态方法调用
  >
  >   ```java
  >   public static ByteBuffer allocateDirect(int capacity) {
  >       return new DirectByteBuffer(capacity);
  >   }
  >   ```
  >
  > - DirectByteBuffer内部实现通过Unsafe 的本地方法 allocateMemory() 进行内存分配，底层调用的是操作系统的 malloc() 函数
  >
  >   [Java魔法类：Unsafe应用解析](https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html)
  >
  > - 初始化 DirectByteBuffer 时还会创建一个 Deallocator 线程，并通过 Cleaner 的 freeMemory() 方法来对直接内存进行回收操作，freeMemory() 底层调用的是操作系统的 free() 函数
  >
  > - 实际上MappedByteBuffer的内存映射底层就是通过反射获取DirectByteBuffer实例进行内存操作
  
  > **FileChannel（实现通道之间数据传输方式）**
  >
  > - 用于文件读写、映射和操作的通道，同时它在并发环境下是线程安全
  >
  > - FileInputStream FileOutputStream 或者 RandomAccessFile 的 getChannel() 方法
  >
  >   可以创建并打开一个文件通道
  >
  > - FileChannel中定义了transferTo和transferFrom抽象方法实现通道之间数据传输
  >
  >   （`sun.nio.ch.FileChannelImpl.java` 类负责实现）
  >
  >   ```java
  >   //通过 FileChannel 把文件里面的源数据写入一个 WritableByteChannel 的目的通道
  >   public abstract long transferTo(long position, long count, WritableByteChannel target) throws IOException;
  >   
  >   //把一个源通道 ReadableByteChannel 中的数据读取到当前 FileChannel 的文件里面
  >   public abstract long transferFrom(ReadableByteChannel src, long position, long count) throws IOException;
  >
  >
  >   以上两者底层实现都是通过OS进行系统调用sendfile()函数实现零拷贝操作
  >
  >   ```

### 多路复用IO的优缺点

- ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-aio-2.png)不再使用多线程进行IO处理（对OS的内核IO而言，应用进程还是可以引入线程池技术)
- 同一个端口可以处理多种协议（ServerSocketChannel既可以处理TCP协议又可以处理UDP协议）
- 操作系统级别优化：同一端口同时接受多个客户端的IO事件（兼具阻塞式同步IO和非阻塞式同步IO特点）
- 类属同步IO：阻塞式IO、非阻塞式IO、多路复用IO，都是基于OS级别对“同步IO”的实现（同步IO不会主动告知上层事件发生）

### AIO通信方式（订阅-通知）

- 即应用程序向操作系统注册IO监听，然后继续做自己的事情
- 当OS发生IO事件，且准备好数据后主动通知应用程序，触发相应的函数

![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-aio-1.png)

- 跟同步IO一样，底层依赖操作系统进行异步IO支持
  - windows提供一种异步IO技术（IOCP 即 I/O Completion Port， I/O完成端口）
  - Linux则通过Epoll对异步IO进行模拟实现

### Java对AIO的支持

- jvm根据不同的操作系统实现AsynchronousXXXChannelImpl支持

  ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/io/java-io-aio-2.png)
