### Java多线程与并发

#### 理论基础

- 为什么需要多线程?

  答: CPU/Memory/IO设备之间的速度差，为了充分利用CPU的高性能

  计算机体系结构为CPU增设多级缓存   <!--导致可见性问题-->

  操作系统增加了进程/线程/分时复用CPU，均衡CPU和IO设备速度差   <!--导致原子性问题-->

  编译程序优化指令执行次序，为了更合理利用缓存   <!--导致有序性问题-->

- Java是如何解决并发问题? JMM（Java内存模型规范 ）

  <!--JVM提供按需禁用 缓存和编译优化 的方法保证可见性|有序性|原子性-->

  - volatile  synchronized final 三个关键字

    volatile保证一定有序性和可见性

    synchronized加锁不存在并发问题

    final修饰的属性会禁止特殊重排序

    - final域写：禁止final域写重排序到构造方法外
    - final域读：禁止初次读对象的引用与读该对象包含的final域的重排序
    - 引用数据类型额外增加约束：禁止（构造函数对一个final对象的赋值）与（final对象的引用赋值给其它引用）重排序

  - happens-before 规则（主要为保证有序性）
    1. 单线程内程序前面的操作先行发生于后面的操作（单一线程原则）
    2. 一个unlock的操作先行发生于对同一个锁的lock操作（管程锁定原则）
    3. 对一个volatile变量的写操作先行发生于对这个变量的读操作（volatile变量规则）
    4. Thread对象的start()方法调用先行发生于此线程的每一个动作（线程启动规则）
    5. Thread对象的结束先行发生于join()方法返回（线程加入规则）
    6. 对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生，可以通过 interrupted() 方法检测到是否有中断发生（线程中断规则）
    7. 一个对象的初始化完成(构造函数执行结束)先行发生于它的 finalize() 方法的开始（对象终结规则）
    8. 如果操作 A 先行发生于操作 B，操作 B 先行发生于操作 C，那么操作 A 先行发生于操作 C（传递性）

- 线程安全并不是一个非真即假的命题

  <!--根据共享数据的安全程度可分为五类：-->

  - 不可变(Immutable)对象一定是线程安全的

    - final关键字修饰的基本数据类型
    - String对象
    - enum枚举
    - Number部分子类（Long|Double|BigInteger|BigDeciaml）,同为 Number 的原子类 AtomicInteger 和 AtomicLong 则是可变的
    - 使用 Collections.unmodifiableXXX() 方法来获取一个不可变的集合

  - 绝对线程安全，调用者无需额外同步措施

  - 相对线程安全，调用者只需保证对此对象调用的操作是线程安全的，调用中无需担心线程安全问题

    例如 Vector  HashTable  Collections的synchronizedCollection()方法包装的集合

  - 线程兼容，对象本身不是安全的，但可以在调用端正确使用同步手段保证并发安全

    例如 Vector 和 HashTable 相对应的集合类 ArrayList 和 HashMap 等

  - 线程对立，指无法在多线程环境中并发使用的代码，并当避免

- 线程安全的实现方法
  1. 互斥同步/阻塞同步（悲观）- synchronized和ReentrantLock
  2. 非阻塞同步（乐观）
     - CAS
     - AtomicXXX原子类
     - ABA + 版本
  3. 无同步方案（如果方法本身不涉及共享数据，那么就无需同步措施）
     - 栈封闭（局部变量是线程栈私有）
     - 线程本地存储(Thread Local Storage)，每个线程修改的都是自己的那份共享数据
     - 可重入代码(Reentrant Code)，也叫纯代码(Pure Code)，这种代码执行时允许随时中断/恢复

#### 线程基础

- 线程状态及变化方式

  ![](https://pdai.tech/_images/pics/ace830df-9919-48ca-91b5-60b193f593d2.png)

- 线程使用方式
  1. Runnable接口，通过Thread来调用
  2. Callable接口，通过Thread来调用
  3. 继承Thread类（不推荐，java只支持单继承却支持多实现，且继承整个Thread类开销过大）

- 基础线程机制有哪些？
  1. Executor管理（CachedThreadPool  FixedThreadPool  SingleThreadExecutor）
  2. Daemon线程
  3. sleep()
  4. yield()
  
- 线程的中断方式有哪些?
  1. 当线程任务完成时会中断
  
  2. 当线程任务出现异常时会中断
  
  3. 手动调用interrupt()迫使抛出InterruptedException异常中断，但是I/O阻塞、synchronized锁阻塞、死循环时中断无效

     ```java
     interrupt()：将调用该方法的对象所表示的线程标记一个停止标记，并不是真的停止该线程。是一个实例方法。
     interrupted()：获取当前线程的中断状态，并且会清除线程的状态标记。是一个是静态方法。
     isInterrupted()：获取调用该方法的对象所表示的线程，不会清除线程的状态标记。是一个实例方法。
     ```
  
  4. 调用 Executor 的 shutdownNow() 方法，相当于调用每个线程的 interrupt() 方法
  
  5. 当只想中断Executor中的某一个线程，使用submit() 方法来提交一个线程，它会返回一个 Future<?> 对象，通过调用该对象的 cancel(true) 方法就可以单独中断该线程
  
- 线程互斥同步
  1. synchronized（jvm层面实现、等待过程不可中断、绝对非公平的、只有一个上锁条件；1.6之前无论并发程序是否存在竞争，都会调用OS的mutex）
  2. ReentrantLock（jdk层面实现、等待过程可中断、支持公平/非公平、允许绑定多个Condition对象；如果并发程序不存在竞争，则不会调用OS的park）
  
- 线程之间协作
  1. Thread的join()
  2. Object的wait() notify() notifyAll()
  3. Condition的await() signal() signalAll()
  
  - *[协作方式对比](https://pdai.tech/md/java/thread/java-thread-x-lock-LockSupport.html#%E6%9B%B4%E6%B7%B1%E5%85%A5%E7%9A%84%E7%90%86%E8%A7%A3)*

#### 加锁与释放锁

![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/thread/java-thread-x-key-schronized-2.png)

- 锁重入

  monitorenter指令会让对象的锁计数器+1，如果此线程已拿到这个锁的所有权，又重入这把锁，那锁计数器就会累加，但不会再执行monitorenter指令

  monitorexit指令会让对象的锁计数器-1，如果减完后不为0，表明刚才发生重入锁，那当前线程继续持有这把锁的所有权，如果减完为0则释放锁

#### [JVM锁优化](https://pdai.tech/md/java/thread/java-thread-x-key-synchronized.html#jvm%E4%B8%AD%E9%94%81%E7%9A%84%E4%BC%98%E5%8C%96)

简单来说monitorenter和monitorexit指令依赖底层OS的互斥锁（Mutex Lock）实现，执行互斥锁需要OS将当前线程挂起然后从用户态转内核态来加/解锁；

每次都无条件调用互斥锁将对严重影响程序性能，因此JDK1.6对锁的实现引入了大量优化：

1. **锁粗化(Lock Coarsening)**：也就是减少不必要的紧连在一起的unlock，lock操作，将多个连续的锁扩展成一个范围更大的锁。

2. **锁消除(Lock Elimination)**：通过运行时JIT编译器的逃逸分析来消除一些没有在当前同步块以外被其他线程共享的数据的锁保护，通过逃逸分析也可以在线程本地Stack上进行对象空间的分配(同时还可以减少Heap上的垃圾收集开销)

   <!--不涉及线程安全的代码加了同步操作没有意义-->

3. **轻量级锁(Lightweight Locking)**：这种锁实现的背后基于这样一种假设，即在真实的情况下我们程序中的大部分同步代码一般都处于无锁竞争状态(即单线程执行环境)，在无锁竞争的情况下完全可以避免调用操作系统层面的重量级互斥锁，取而代之的是在monitorenter和monitorexit中只需要依靠一条CAS原子指令就可以完成锁的获取及释放。当存在锁竞争的情况下，执行CAS指令失败的线程将调用操作系统互斥锁进入到阻塞状态，当锁被释放的时候被唤醒

   <!--自旋锁默认的自旋次数为10次，用户可以使用参数`-XX:PreBlockSpin`来更改-->

   ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/thread/java-thread-x-key-schronized-7.png)

4. **偏向锁(Biased Locking)**：是为了在无锁竞争的情况下避免在锁获取过程中执行不必要的CAS原子指令，因为CAS原子指令虽然相对于重量级锁来说开销比较小但还是存在非常可观的本地延迟

   <!--当一个锁不存在多线程竞争，而是被同一个线程反复获取锁释放锁-->

   ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/thread/java-thread-x-key-schronized-9.png)

5. **适应性自旋(Adaptive Spinning)**：当线程在获取轻量级锁的过程中执行CAS操作失败时，在进入与monitor相关联的操作系统重量级锁前会进入忙等待(Spinning)然后再次尝试，当尝试一定的次数后如果仍然没有成功则调用与该monitor关联的互斥锁进入到阻塞状态

   <!--适应性自旋锁是自旋锁（轻量级锁）的改进，自旋次数或着是否自旋：由上一次在同一个锁上的自旋时间及锁的拥有者的状态决定-->

#### 锁升级及锁类型比较

Java SE 1.6里同步锁，一共有四种状态：`无锁`>`偏向锁`>`轻量级所`>`重量级锁`，它会随着竞争情况逐渐升级。

锁可以升级但是不可以降级，目的是为了提供获取锁和释放锁的效率。

| 锁       | 实现原理                                                     | 优点                                                         | 缺点                                                         | 使用场景                           |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------- |
| 偏向锁   | 在对象头中存在Mark Word，其中有2bit用于锁标记位，每个线程的栈帧中还有锁记录(Lock Record)空间，记录锁对象的Mark Word并更新其锁标记位 | 加锁和解锁不需要CAS操作，没有额外的性能消耗，和执行非同步方法相比仅存在纳秒级的差距 | 如果线程间存在锁竞争，会带来额外的锁撤销的消耗               | 适用于只有一个线程访问同步块的场景 |
| 轻量级锁 | 在对象头中存在Mark Word，其中有是否为偏向锁标记位，每个线程的栈帧中还有锁记录(Lock Record)空间，记录锁对象的Mark Word并更新其偏向锁标记位 | 竞争的线程不会阻塞，提高了响应速度                           | 如线程成始终得不到锁竞争的线程，使用自旋会消耗CPU性能        | 追求响应时间，同步块执行速度非常快 |
| 重量级锁 | 依赖OS的Mutex Lock                                           | 线程竞争不适用自旋，不会消耗CPU                              | 线程阻塞，响应时间缓慢，在多线程下，频繁的获取释放锁，会带来巨大的性能消耗 | 追求吞吐量，同步块执行速度较长     |

#### [JUC包下类汇总](https://pdai.tech/md/java/thread/java-thread-x-juc-overview.html#overview)

![JUC类汇总](https://pdai.tech/_images/thread/java-thread-x-juc-overview-1.png)

- locks和tools类关系总览

  ![](https://pdai.tech/_images/thread/java-thread-x-juc-overview-lock.png)

  - Condition接口：实现了类似Object类内置线程协作的相关方法，与任意Lock实现组合使用

  - Lock接口：实现比synchronized语法更广泛灵活的锁操作，可以支持多个Condition对象

  - ReadWriteLock接口：维护一对相关的锁。读锁共享，写锁独占

  - AbstractOwnableSynchonizer抽象类：为一切线程独占（所有权）同步器奠定基础概念

    `private transient Thread exclusiveOwnerThread; \\当前持有锁的线程`

  - AbstractQueuedLongSynchronizer抽象类：AbstractQueuedSynchonizer抽象类的long版本

  - **AbstractQueuedSynchonizer**核心抽象类：为实现依赖于先进先出 (FIFO) 等待队列的阻塞锁和相关同步器(信号量、事件、等等...)提供一个框架。此类的设计目标是成为依靠单个原子 int 值来表示状态的大多数同步器的一个有用基础

    <!--AQS框架实现借助两个类：Unsafe提供CAS 和 LockSupport提供线程挂起恢复-->

    - 核心思想

      case（共享资源空闲）then

      ​	将当前请求资源的线程设置为有效的工作线程，并锁定共享资源

      ​	`private volatile int state; //表示同步状态,通过CAS修改`

      case（共享资源锁定）then

      ​	通过一套线程阻塞等待以及被唤醒时锁分配机制管理（CLH队列锁）

    - 资源共享方式

      1. Exclusive(独占)：有且仅有一个工作线程，如ReentrantLock。

         其余线程按锁抢占机制又可分为：

         - 公平锁：按线程在队列中顺序先后拿锁
         - 非公平锁：无视队列顺序直接去抢锁

      2. Share(共享)：允许多个线程可同时执行，如Semaphore、CountDownLatch、CyclicBarrier。

      <!-- ReadWriteLock下的实现ReentrantReadWriteLock可看成是组合式，
           ReentrantReadWriteLock.WriteLock是Exclusive的，
           ReentrantReadWriteLock.ReadLock是Share的。-->

    - 实现设计模式 - 模板方法，自定义同步器时需要重写下面几个AQS提供的模板方法

      ```java
      //默认情况下，以下每个方法都会抛出UnsupportedOperationException。
      //同时自定义的同步器对这些方法的实现必须是内部线程安全的，并且通常应该简短而不是阻塞。
      isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
      tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
      tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
      tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
      tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
      ```

    - 内部数据结构

      CLH(Craig,Landin,and Hagersten)队列，它是一个虚拟的双向队列(不存在队列实例，仅存在结点间的关联关系)。AQS是将每条请求共享资源的线程封装成一个CLH锁队列的一个结点(Node)来实现锁的分配。

      ```java
      static final class Node {
          // 资源共享模式，分为共享与独占
          static final Node SHARED = new Node();
          static final Node EXCLUSIVE = null;        
          // 结点状态
          // CANCELLED，值为1，表示当前的线程被取消
          // SIGNAL，值为-1，表示当前节点的后继节点包含的线程需要运行，也就是unpark
          // CONDITION，值为-2，表示当前节点在等待condition，也就是在condition队列中
          // PROPAGATE，值为-3，表示当前场景下后续的acquireShared能够得以执行
          // 值为0，表示当前节点在sync队列中，等待着获取锁
          static final int CANCELLED =  1;
          static final int SIGNAL    = -1;
          static final int CONDITION = -2;
          static final int PROPAGATE = -3;        
      
          // 结点状态
          volatile int waitStatus;        
          // 前驱结点
          volatile Node prev;    
          // 后继结点
          volatile Node next;        
          // 结点所对应的线程
          volatile Thread thread;        
          // 下一个等待者
          Node nextWaiter;
          
          // 结点是否在共享模式下等待
          final boolean isShared() {
              return nextWaiter == SHARED;
          }
          
          // 获取前驱结点，若前驱结点为空，抛出异常
          final Node predecessor() throws NullPointerException {
              // 保存前驱结点
              Node p = prev; 
              if (p == null) // 前驱结点为空，抛出异常
                  throw new NullPointerException();
              else // 前驱结点不为空，返回
                  return p;
          }
          
          // Used to establish initial head or SHARED marker
          Node() {}
          
          // Used by addWaiter
          Node(Thread thread, Node mode) {  
              this.nextWaiter = mode;
              this.thread = thread;
          }
          
          // Used by Condition
          Node(Thread thread, int waitStatus) {
              this.waitStatus = waitStatus;
              this.thread = thread;
          }
      }
      ```

      AQS通过CLH数据结构实现了两种同步队列：

      ![](https://pdai.tech/_images/thread/java-thread-x-juc-aqs-1.png)

      1. Sync queue，即同步队列，是双向链表，包括head结点和tail结点，head结点主要用作后续的调度。

         ```java
         public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
             implements java.io.Serializable {
             private transient volatile Node head;
             private transient volatile Node tail;
         }
         ```

      2. Condition queue，其不是必须的，其是一个单向链表，只有当使用Condition时，才会存在此单向链表。并且可能会有多个Condition queue。

         ```java
         public class ReentrantLock implements Lock, java.io.Serializable {
             private final Sync sync;
             abstract static class Sync extends AbstractQueuedSynchronizer {
                 final ConditionObject newCondition() {
                     return new ConditionObject();
                 }
             }
             //用户调用newCondition()方法就会产生Condition Queue
             public Condition newCondition() {
                 return sync.newCondition();
             }
         }
         public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
             implements java.io.Serializable {
             public class ConditionObject implements Condition, java.io.Serializable {
                 ...
             }
         }
         ```

    - 核心方法分析

      ```java
      //拿到锁：方法正常返回，拿不到锁或者获取队列失败：自我中断
      public final void acquire(int arg) {
          //tryAcquire()用于处理大部分无竞争关系的并发程序
          //acquireQueued(addWaiter())用于处理存在竞争关系的并发程序该如何竞争
          if (!tryAcquire(arg) &&
              acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
              selfInterrupt();	//响应中断并重置中断状态标识
      }
      
      //负责将竞争线程加入到CLH结构的Sync queue
      private Node addWaiter(Node mode) {
          Node node = new Node(Thread.currentThread(), mode);
          Node pred = tail;
          //如果Sync queue不为空,直接入队尾
          if (pred != null) {
              node.prev = pred;
              if (compareAndSetTail(pred, node)) {
                  pred.next = node;
                  return node;
              }
          }
          //如果Sync queue为空
          //1. 先初始化傀儡队头（代表占有锁工作线程）
          //2. 然后将当前线程入队尾
          enq(node);
          return node;
      }
      
      //先自旋尝试再次拿锁，拿不到再挂起线程
      final boolean acquireQueued(final Node node, int arg) {
          boolean failed = true;
          try {
              boolean interrupted = false;
              //自旋尝试
              for (;;) {
                  final Node p = node.predecessor();
                  //如果自旋成功获取锁（仅针对傀儡队头后第一个元素）
                  if (p == head && tryAcquire(arg)) {
                      setHead(node);
                      p.next = null; // help GC
                      failed = false;
                      return interrupted;
                  }
                  //shouldParkAfterFailedAcquire()作用
                  //1.检测prev节点waitStatus能否触发节点再次自旋
                  //2.延迟进行park系统调用
                  //3.更新前置节点状态为休眠（针对已park前置节点，它无法更新自己状态）
                  //4.为保证解锁机制和唤醒后续节点抢锁机制
                  //parkAndCheckInterrupt()作用
                  //1.挂起当前竞争线程
                  //2.唤醒后检测线程是否被外部中断过
                  //2.1未中断会促使再次自旋
                  //2.2中断过会进行标识,响应中断（AQS取锁排队过程不允许外部中断）
                  //3.唤醒后的线程的中断状态会被置为true,需要重置促使再次自旋
                  if (shouldParkAfterFailedAcquire(p, node) &&
                      parkAndCheckInterrupt())
                      interrupted = true;
              }
          } finally {
              if (failed)
                  cancelAcquire(node);
          }
      }
      
      /**
       * Checks and updates status for a node that failed to acquire.
       * Returns true if thread should block. This is the main signal
       * control in all acquire loops.  Requires that pred == node.prev.
       * @return {@code true} if thread should block
       */
      private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
          int ws = pred.waitStatus;
          if (ws == Node.SIGNAL)
              /*
               * This node has already set status asking a release
               * to signal it, so it can safely park.
               */
              return true;
          if (ws > 0) {
              /*
               * Predecessor was cancelled. Skip over predecessors and
               * indicate retry.
               */
              do {
                  node.prev = pred = pred.prev;
              } while (pred.waitStatus > 0);
              pred.next = node;
          } else {
              /*
               * waitStatus must be 0 or PROPAGATE.  Indicate that we
               * need a signal, but don't park yet.  Caller will need to
               * retry to make sure it cannot acquire before parking.
               */
              compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
          }
          return false;
      }
      
      //用于判断当前线程在公平模式下是否需要排队
      public final boolean hasQueuedPredecessors() {
          Node t = tail;
          Node h = head;
          Node s;
          //1. h != t 用于判断队列元素是否大于1,但队列初始化在并发环境有可能会导致此判断成立
          /*
          	private Node enq(final Node node) {
          		...
          			if (t == null) { // Must initialize
              			if (compareAndSetHead(new Node()))
                      		tail = head;   //这段代码存在并发同步问题: head=newNode tail=null
              		}
              	...
          	}
          */
          //2. ((s = h.next) == null || s.thread != Thread.currentThread()) 用于判断队列元素是否等于1
          //2.1 (s = h.next) == null 用于解决队列是否为初始化一半的特例
          //2.2 s.thread != Thread.currentThread() 用于判断自己是否为傀儡头后首个线程
          return h != t &&
              ((s = h.next) == null || s.thread != Thread.currentThread());
      }
      ```

      ```java
      //释放锁：方法正常返回，队列不为空则唤醒后继节点
      public final boolean release(int arg) {
          if (tryRelease(arg)) {
              Node h = head;
              if (h != null && h.waitStatus != 0)
                  unparkSuccessor(h);
              return true;
          }
          return false;
      }
      
      //
      private void unparkSuccessor(Node node) {
          /*
           * If status is negative (i.e., possibly needing signal) try
           * to clear in anticipation of signalling.  It is OK if this
           * fails or if status is changed by waiting thread.
           */
          int ws = node.waitStatus;
          if (ws < 0)
              compareAndSetWaitStatus(node, ws, 0);
      
          /*
           * Thread to unpark is held in successor, which is normally
           * just the next node.  But if cancelled or apparently null,
           * traverse backwards from tail to find the actual
           * non-cancelled successor.
           */
          Node s = node.next;
          if (s == null || s.waitStatus > 0) {
              s = null;
              for (Node t = tail; t != null && t != node; t = t.prev)
                  if (t.waitStatus <= 0)
                      s = t;
          }
          if (s != null)
              LockSupport.unpark(s.thread);
      }
      ```

    - 总结

      1. 持有锁的线程永远不在Sync queue中（使用傀儡队头代表）
      2. Sync queue的第二个节点表示下次能够拿锁的线程节点

  - **LockSupport**锁常用类：实现类似Thread中suspend()阻塞和resume()解除阻塞，但不会导致死锁问题

    <!--该类的park()和unpark()方法 - 依赖 - Unsafe的park()和unpark()实现-->

    - park()和unpark()的先后调用顺序不影响同步效果
    - park()阻塞不会释放线程上的锁资源（Condition.await()负责释放）
    - park()被另一个线程unpark()唤醒后，一定会继续执行后续内容
    - 如下三种情况会导致已阻塞的线程解除阻塞
      1. 其他某个线程将当前线程作为目标调用unpark()
      2. 其他某个线程中断当前线程
      3. 该调用不合逻辑地(即毫无理由地)返回

  - ReentrantLock锁常用类：具有synchronized语法相同功能，但功能更强大灵活

    ReentrantLock.lockInterruptibly()方法允许打断阻塞线程重新抢锁

    ```java
    //模拟ReentrantLock上锁和解锁
    volatile int state;
    Queue<Thread> queue;
    
    public void lock() {
        while(!cas(0,1)) {
            park();
        }
        unlock();
    }
    public void unlock() {
        state=0;
        notify();
    }
    void park() {
        queue.add(currentThread);
        //当前线程释放CPU并阻塞在此
        LockSupport.park();
    }
    void notify() {
        Thread t = queue.header();
        LockSupport.unpark(t);
    }
    ```

  - ReentrantReadWriteLock锁常用类：ReadWriteLock接口的实现类，通过维护Lock的子类ReadLock和WriteLock实现

  - CountDownLatch工具类：同步辅助类，在一组其他线程完成任务之前，允许阻塞一个或多个线程等待

  - CyclicBarrier工具类：同步辅助类，允许一组线程互相等待，直到到达某个公共屏障点

  - Semaphore工具类：本质上该信号量维护了一个许可集

  - Exchanger工具类：允许两个线程之间的数据交换

- collections并发集合类关系总览

  ![](https://pdai.tech/_images/thread/java-thread-x-juc-overview-2.png)

  - ArrayBlockingQueue：通过数组实现的按FIFO原则的有界阻塞队列

  - LinkedBlockingQueue：通过链表实现的按FIFO原则的无界阻塞队列

  - LinkedBlockingDueue：通过双向链表实现的双端无界阻塞队列

  - ConcurrentLinkedQueue：通过链表实现的按FIFO原则的无界线程安全队列（不允许 null 元素）

  - ConcurrentLinkedDueue：通过双向链表实现的双端无界线程安全队列（不允许 null 元素）

  - DelayQueue：延时无界线程安全阻塞队列，通过Lock机制实现并发安全访问。head是最先到期的元素，如果没到期有元素也取不到（不允许 null 元素）

  - PriorityBlockingQueue：优先级无界线程安全阻塞队列，通过Lock机制实现并发安全访问。（不允许 null 元素，且元素要有可比性）

  - SynchronousQueue：不存储元素的同步队列，通过CAS实现并发访问，支持FIFO（TransferQueue）和FILO（TransferStack）

  - CopyOnWriteArrayList：添加和修改操作都是复制一份后（线程安全的替换）原本实现，并发读次数>并发写次数时效率很高

  - CopyOnWriteArraySet：不同与CopyOnWriteArrayList，添加和修改会调用addIfAbsent遍历数组去重，因此性能会稍逊

  - ConcurrentSkipListSet：基于ConcurrentSkipListMap 的可缩放并发 NavigableSet 实现

  - ConcurrentSkipListMap：相当于线程安全的TreeMap

  - ConcurrentHashMap：线程安全HashMap，JDK 7之前是通过Lock和segment(分段锁)实现，JDK 8 之后改为CAS+synchronized来保证并发安全

    

- executors线程池类关系总览

  ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/thread/java-thread-x-juc-executors-1.png)

  - Executors工具类：负责创建线程池服务，线程池工厂配置等
  - Executor接口：提供一种"将任务提交"与"每个任务如何运作的机制"的抽象方法
  - ExecutorService接口：继承自Executor，负责管理任务的提交和起止，以及为异步任务生成Future的能力
  - ScheduledExecutorService接口：继承自Executor，定时或延迟版的ExecutorService
  - AbstractExecutorService抽象类：ExecutorService接口方法的默认实现类
  - FutureTask类：实现Callable和Runnable任务的异步能力，其线程安全由CAS来保证
  - ThreadPoolExecutor类：AbstractExecutorService的实现类，提供线程池来执行任务（通过Executors的工厂方法进行线程池配置）
  - ScheduledThreadExecutor类：定时或延迟版的ThreadPoolExecutor
  - Fork/Join框架：JDK7加入的一个线程池类，采用分治算法且能并行执行，充分利用多核CPU提高运算性能

- [13个atomic原子类](https://pdai.tech/md/java/thread/java-thread-x-juc-overview.html#atomic-%E5%8E%9F%E5%AD%90%E7%B1%BB)

  实现原理：volatile字段 + CAS

  - AtomicStampedReference如何解决ABA问题?

    该类存在一个私有静态内部Pair类，Pair类有一个可以自动更新整数stamp和引用元素值reference

    每次调用AtomicStampedReference.compareAndSet()会判断stamp和reference是否改变

    如果改变了其一则构造新的Pair对象并调用UNSAFE.CAS进行更新

#### CAS依赖Unsafe类实现

- Compare-And-Swap

  CAS操作是一条CPU的原子指令（cmpxchg指令），JVM封装此基于硬件平台的汇编指令

  1. ABA问题 - 引入版本号

  2. 自旋长时间不成功影响CPU开销 - 如果jvm能支持处理器提供的pause指令

     <!--pause指令作用：
         第一，延迟处理器流水线执行命令 
         第二，避免退出循环时出现内存顺序冲突而引起处理器流水线执行命令被清空
     -->

  3. 不能保证多个共享变量的原子操作 - 使用锁或把多个共享变量合并成一个共享变量(AtomicReference)

- sun.misc.Unsafe类

  提供不安全的底层操作方法，如直接访问系统内存资源、自主管理内存资源等

  内部使用自旋方式实现CAS（while循环进行CAS更新，如果更新失败，则循环再次重试）

  ```java
  //以下3个方法是Unsafe类支持的原子操作
  
  public final native boolean compareAndSwapObject(Object paramObject1, long paramLong, Object paramObject2, Object paramObject3);
  
  public final native boolean compareAndSwapInt(Object paramObject, long paramLong, int paramInt1, int paramInt2);
  
  public final native boolean compareAndSwapLong(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
  ```

  [Unsafe类功能划分如下图所示：了解详情点击链接](https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html)

  ![UnSafe类总体功能](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/thread/java-thread-x-atomicinteger-unsafe.png)
