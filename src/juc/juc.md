##JUC并发编程
1. 什么是JUC
   + juc 全称 **java.util.concurrent**
   + 专门用于处理多线程环境下的线程安全问题
2. 线程和进程
   > 进程和线程的区别
   + 一个程序就是一个进程,一个进程至少包含一个线程
   + 进程是系统资源的最小调度单位,线程是CPU资源的最小调度单位
   > java的线程
   + java默认线程2个 : gc 和 main
   + java不能主动开启线程,调用本地的start0()方法
   > 并发和并行
   + **并发** : 模拟宏观上并行,实际微观上串行
   + **并行** : 多核CPU真正实现线程并行处理
   ```
   int processors = Runtime.getRuntime().availableProcessors()
   ```
   > 线程状态
   1. Java线程6大状态
      + NEW
      + RUNNABLE
      + BLOCKED
      + WAITING
      + TIMED_WAITING -- ***操作系统无此状态***
      + TERMINATED
   2. Object.wait() 和 Thread.sleep() 的区别
      1. 方法的来自不同的类
      2. 执行范围不同
         * wait必须在synchronized上执行,即有锁情况下
         * sleep可以在任何情况下执行
      3. 锁的释放不同
         * wait会释放在其身上的锁,进入永久等待队列
         * sleep如果身上有锁不会释放,进入计时等待队列
3. 生产者和消费者问题
    1. synchronized版本 -- 解决生产者消费者问题
        ```
            synchronized(obj){
                //要用while不要用if(condition),防止虚假唤醒问题
                //比如两个线程同时执行完if判断就会导致虚假唤醒
                //等待应该在循环中进行
                while(condition){
                  obj.wait();
                }
                /**
                  业务代码
                **/
                obj.notify();
            }
        ```
    2. juc版本 -- 解决生产者消费者问题
        ```
            Lock lock = new 锁;
            Condition condition = 锁.同步监视器;
          
            lock.lock();
            try {
                while (condition) {
                    condition.await();
                }
                /**
                  业务代码
                **/
                condition.signal();
            } catch (InterruptedException e) {} 
            finally {
                lock.unlock();
            }
        ```
4. **Lock锁**
    > synchronized关键字
    1. 自动创建锁
    2. 无需手动加锁解锁
    3. 其他线程拿不到锁会永久等待
    4. 适合少量的同步代码块
    5. 本质是 : ***可重入锁,不可被中断,非公平锁***

    > java.util.concurrent.locks.Lock 接口
    1. 手动创建Lock实现类
    2. 手动加锁解锁,否则会死锁
    3. 其他线程拿不到锁可以尝试取锁 `lock.tryLock();`
    4. 适合大量的同步代码块
    5. 自行决定是否公平锁等
    + **java.util.concurrent.locks.ReentrantLock** 实现类(可重入锁)
    ```
    # 公平锁和非公平锁的区别
    # 公平锁: 十分公平,必须队列排队执行,分先来后到
    # 非公平锁: 十分不公平,可以插队执行,效率更高
    /**
    * 可重入锁构造函数
    * 默认创建的是非公平锁
    */
    public ReentrantLock() {
        sync = new NonfairSync();
    }
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
    ```
    + **java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock** 实现类(读锁/共享锁)
    + **java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock** 实现类(写锁/独占锁)
5. **ReadWriteLock读写锁**
    > java.util.concurrent.locks.ReadWriteLock 接口
    1. 维护了一对互斥的锁: 读锁和写锁
    2. 读锁: 以由多个 reader 线程同时保持共享数据
    3. 写锁: 只有一个线程 writer 线程可以修改共享数据
    4. 写锁一旦触发,会使所有读锁进行阻塞
    5. 读锁一旦触发,会允许所有读但阻塞写
    5. 适用于读取频繁的操作
    + **java.util.concurrent.locks.ReentrantReadWriteLock** 实现类(可重入读写锁)
6. 8锁现象(如何判断锁的对象是谁)
    1. synchronized修饰在同步代码块中
       * 锁对象: 通过代码块中的锁对象(可以使用任意的对象)
       * 注意: 必须保证多个线程使用的锁对象是同一个对象
    2. synchronized修饰在普通方法中
       * 锁对象: 方法调用者(this)
    3. synchronized修饰在静态方法中
       * 锁对象: 类的class对象(Heap中存储)
7. 集合类的非线程安全
    > 解决方案
    1. java.util.Vector 和 java.util.HashTable (其操作方法都是synchronized修饰的)
    2. Collections.synchronizedXXX(new 非线程安全集合)
    3. java.util.concurrent.线程安全集合
8. Callable 和 Runnable
    > Callable 和 Runnable 都是线程执行而设计的类
    1. Callable 可以有返回值
    2. 能抛出被检查的异常
    > Callable 如何被Thread类调用?
    1. 首先Thread线程类只接受Runnable接口类的构造方法
    `public Thread(Runnable target) {}`
    2. 但是java.util.concurrent包下的FutureTask类实现了Runnable接口
    `class FutureTask<V> implements RunnableFuture<V> extends Runnable`
    3. 而且FutureTask类接受Callable接口类的构造方法
    `public FutureTask(Callable<V> callable) {}`
    4. 最后FutureTask类实现了Runnable接口的run()方法,并在里面调用Callable.call()方法获取返回值保存
    5. FutureTask类里面的get()方法可以获取返回值,返回值会被缓存提高效率
    `注意:此方法会被阻塞,如果call方法还在执行中`
9. 并发包下CountDownLatch CyclicBarrier Semaphore的作用
    > CountDownLatch 减法计数器 -- 允许一个或者多个线程等待直到其他线程中一组操作完成的同步辅助类
      * 计数器递减操作
      `CountDownLatch.countDown()`
      * 等待计数器归零,await()被唤醒代码才能向下执行
      `CountDownLatch.await()`
    > CyclicBarrier 加法计数器 -- 允许一组线程全部等待彼此达到共同屏障点的同步辅助类
      * 计数器累计操作,await()之后加1,如果未达到执行值则本条线程就会被阻塞,直到计数器累计到执行值
      `CyclicBarrier.await()`
      * 如果构造函数传参,还给了Runnable则计数器累计到执行值后,会执行Runnable的run
      `public CyclicBarrier(int parties, Runnable barrierAction)`
    > Semaphore 记录型信号量 -- 允许指定数量内的线程进入执行,其他进入等待队列
      * 允许进入的指定数量
      `public Semaphore(int permits)`
      * 指定数量的减少和增加
      ```
        public void acquire() //调用后可进入线程数量-1
        public void release() //调用后可进入线程数量+1
      ```
10. 阻塞队列
    > 队列不得不阻塞的情况
    1. 队列满无法入队时,需要等待出队
    2. 队列空无法出队时,需要等待入队
    > java.util.concurrent.BlockingQueue 接口
    + **java.util.concurrent.ArrayBlockingQueue** 实现类(数组阻塞队列)
    + **java.util.concurrent.LinkedBlockingQueue** 实现类(链表阻塞队列)
    + |  /  | 抛出异常 | 屏蔽异常,有返回值 | 永久阻塞等待 | 超时等待 |
      | ---- | -------- | ----------------- | ------------ | -------- |
      |  添加  |    add(E e)    |    offer(E e)    |    put(E e)    |    offer(E e,long timeout,TimeUnit unit)    |
      |  删除  |    remove()    |    poll()    |    take()    |    poll(long timeout,TimeUnit unit)    |
      |  队首元素获取  |    element()    |    peek()    |    /    |    /    |
    + **java.util.concurrent.PriorityBlockingQueue** 实现类(优先阻塞队列)
    1. 内部只能够包含一个元素
    2. 调用put()插入元素后线程被阻塞,直到其他线程take()取出元素
    3. 调用take()取不到元素线程被阻塞,直到其他线程put()插入元素
    4. 可以明确是否使用公平锁 `public SynchronousQueue(boolean fair)`
    5. 实际队列内部不会存储元素,所以尽量避免使用add|offer此类有返回值的方法
    + **java.util.concurrent.SynchronousQueue** 实现类(同步队列)
    
    > java.util.concurrent.BlockingDeque extends BlockingQueue 接口
    + **java.util.concurrent.LinkedBlockingDeque** 实现类(链表阻塞双端队列)
11. 线程池
    > 三大方法创建线程池(alibaba不推荐用)
    1. 创建单例线程的池: `Executors.newSingleThreadExecutor();`
    2. 创建固定线程的池: `Executors.newFixedThreadPool(int nThreads);`
    3. 创建可扩容线程的池: `Executors.newCachedThreadPool();`
    > 七大参数自定义线程池
    + **Executors.newXXX底层调用ThreadPoolExecutor七个参数的构造函数**
    ```
        public ThreadPoolExecutor(int corePoolSize,   //核心恒运行线程数
                                      int maximumPoolSize,   //最大可运行线程数
                                      long keepAliveTime,   //超时销毁
                                      TimeUnit unit,   //计时单位
                                      BlockingQueue<Runnable> workQueue,   //存储线程的阻塞队列
                                      ThreadFactory threadFactory,   //线程池的创建工厂类
                                      RejectedExecutionHandler handler)  //拒绝执行策略
        {
            if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize ||
                keepAliveTime < 0)
                throw new IllegalArgumentException();
            if (workQueue == null || threadFactory == null || handler == null)
                throw new NullPointerException();
            this.acc = System.getSecurityManager() == null ?
                    null :
                    AccessController.getContext();
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.workQueue = workQueue;
            this.keepAliveTime = unit.toNanos(keepAliveTime);
            this.threadFactory = threadFactory;
            this.handler = handler;
        }
        /*
            线程池运行策略:
            1. corePoolSize - 标志此线程池恒运行的线程数
            2. workQueue - corePollSize线程数忙不过来就会放入阻塞队列中
            3. maximumPoolSize - 当workQueue阻塞队列满,则开启创建(maximumPoolSize-corePoolSize)个线程池帮忙
            4. handler - 当maximumPoolSize线程数忙不过来,并且workQueue阻塞队列也满(此时达到最大并发数),就会启用拒绝执行策略
            5. keepAliveTime - 当超过固定时间之后,还没有人使用(maximumPoolSize-corePoolSize)创建的这些线程,将它销毁
        */
    ```
    > 四种线程池拒绝执行策略
    + **java.util.concurrent.RejectedExecutionHandler 接口**
    1. AbortPolicy - 忽视任务并且抛出异常
    2. DiscardPolicy - 忽视任务但不抛出异常,后续提交的任务也忽视
    3. CallerRunsPolicy - 让调用线程池的线程处理任务
    4. DiscardOldestPolicy - 抛弃队列最老任务替代之,并尝试execute它,但如果已经shutdown了就不管
    > CPU密集型和IO密集型  -- maximumPoolSize如何定义?
    1. CPU密集型任务 -- CPU几核就设置成多少,充分利用多核并行
    `maximumPoolSize = Runtime.getRuntime().availableProcessors()`
    2. IO密集型任务 -- 设置得比IO任务数大即可,防止IO阻塞
    `maximumPoolSize > IOTasks[].size`
12. 四大函数式接口
    + 函数式接口: 只有一个方法的接口
    1. Consumer 消费型 - 对输入的参数进行个人程序要求处理
    ```
        public interface Consumer<T> {
            void accept(T t);
        }
    ```
    2. Supplier 供给型 - 要求提供指定参数类型的数据
    ```
        public interface Supplier<T> {
            T get();
        }
    ```
    3. Function 函数型 - 输入指定的参数类型K,输出指定的参数类型V
    ```
        public interface Function<T, R>{
            R apply(T t);
        }
    ```
    4. Predicate 断定型 - 根据个人程序要求对参数进行断言
    ```
        public interface Predicate<T>{
            boolean test(T t);
        }
    ```
13. Stream流式思想
    * 并行流底层同样使用Fork/Join
14. ForkJoin分支合并
    + ForkJoin用途: JDK1.7++,用于并行执行大数据量的任务,提高效率!
    1. 将任务拆分成多个子任务分支并行执行
    2. 各个子分支的结果再返回给它父分支进行结果集合并
    + ForkJoin特点: 任务窃取 -- 自己的任务做完了去偷别的分支的任务做
    1. 每个任务线程都维护一个双端队列
    2. 空闲线程主动去将负载高线程的任务出队执行
    > 核心类
    * java.util.concurrent.ForkJoinPool -- 提供线程池支持ForkJoinTask.
    * java.util.concurrent.ForkJoinWorkerThread -- 线程池中工作的线程类.
    * abstract java.util.concurrent.ForkJoinTask<> -- 负责指定任务和任务拆分合并.
      + abstract java.util.concurrent.RecursiveTask<> extends ForkJoinTask<>
      + abstract java.util.concurrent.RecursiveAction extends ForkJoinTask<>
      + abstract java.util.concurrent.CountedCompleter extends ForkJoinTask<>
    ```
        //使用时需要实现ForkJoinTask.compute() 方法
        ForkJoinTask.fork() 方法 -- 从异步执行的任务启动一个新的子任务
        ForkJoinTask.join() 方法 -- 让异步执行的任务等待兄弟异步任务的完成
    ```
    
15. 异步回调
    > java.util.concurrent.Future<V> 接口 -- 为异步任务建模
    1. 支持异步get()获取结果
    2. 不建议直接调用get(),一调用就会阻塞当前线程直到有结果
    * java.util.concurrent.CompletableFuture<T> implements Future<V> 实现类
        + 当有2个以上的线程试图同时调用 complete()|completeExceptionally()|cancel() 其中一个方法,仅会有一个线程成功
        ```
            //1. 如果无需返回值,可以调用CompletableFuture.runAsync方法,泛型记得给Void 
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    
                }
            })
            //thenRun会在run()任务执行完毕之后进行,它不关心执行结果
            .thenRun(new Runnable() {
               @Override
               public void run() {
                    
               }
            })
            //如果此异步任务执行失败会来到这里
            .exceptionally(new Function<Throwable, Void>() {
               @Override
               public Void apply(Throwable throwable) {
                   System.out.println("error: " + throwable.getMessage());
                   return null;
               }
            });
            
            //2. 如果需要返回值,可以调用CompletableFuture.runAsync方法
            CompletableFuture<T> future = CompletableFuture.supplyAsync(new Supplier<T>() {
                @Override
                public T get() {
                    /*dosomething*/
                    return T;
                }
            })
            //whenComplete会在run()任务成功执行完毕之后进行,然后就可以在此获取执行结果了
            .whenComplete(new BiConsumer<T, Throwable>() {
                @Override
                public void accept(T t, Throwable throwable) {
                    
                }
            })
            //如果此异步任务执行失败会来到这里,并且可以有返回值
            .exceptionally(new Function<Throwable, T>() {
                @Override
                public T apply(Throwable throwable) {
                    /*dosomething*/
                    return T;
                }
            });
        ```
    * java.util.concurrent.FutureTask<T> implements Future<V> 实现类
        + 支持直接传入Runnable任务和Callable任务的构造器
        ```
            public FutureTask(Callable<V> callable)
            public FutureTask(Runnable runnable, V result)
        ```
    * java.util.concurrent.ForkJoinTask<T> implements Future<V> 实现类
    
16. JMM(Java Memory Model)
    ![Java内存模型](https://upload-images.jianshu.io/upload_images/4222138-96ca2a788ec29dc2.png "Java内存模型")
    * 原子性: 即一个操作或者多个操作,要么全部执行并且执行的过程不会被任何因素打断,要么就都不执行
    * 可见性: 可见性是指当多个线程访问同一个变量时,一个线程修改了这个变量的值,其他线程能够立即看得到修改的值
    * 有序性: 即保证程序执行的顺序按照代码的先后顺序执行
        + (指令重排序-instruction reorder): 将源码编译成机器指令的过程中,处理器和编译器为了优化效率可能会
        对不存在数据依赖性的代码进行执行顺序优化,但绝对会保证单个线程的执行结果与预期一致
        + 但是在多线程环境下,即使保证单个线程的执行结果与顺序预期一致,但合并起来却有可能出现问题
        ```
            //线程1:
            context = loadContext();   //语句1
            inited = true;             //语句2 -- 如果这里执行顺序被优化提前到loadContext(),那么线程2就会出问题
            //线程2:
            while(!inited ){
              sleep()
            }
            doSomethingwithconfig(context);
        ```
    * JMM关于内存间交互操作的8种操作指令(原子性)
        + lock(锁定): 作用于主内存的变量,它把一个变量标识为一条线程独占的状态
        + unlock(解锁): 作用于主内存的变量,它把一个处于锁定状态的变量释放出来,释放后的变量才可以被其他线程锁定
        + read(读取): 作用于主内存变量,它把一个变量的值从主内存传输到线程的工作内存,以便随后的load操作使用
        + load(载入): 作用于工作内存的变量,它把read操作从主内存中得到的变量值放入工作内存的变量副本中
        > 如果要把一个变量从主内存拷贝到工作内存,必须要按顺序执行read和load操作,但不要求连续执行,可以插入其他指令
        + use(使用): 作用于工作内存的变量,它把工作内存中的一个变量的值传递给执行引擎,每当虚拟机遇到一个需要使用变量的值的字节码指令时就会执行该操作
        + assign(赋值): 作用于工作内存的变量,它把一个从执行引擎接收的值赋值给工作内存的变量,每当虚拟机遇到一个给变量赋值的字节码指令时就会执行该操作
        + store(存储): 作用于工作内存的变量,它把工作内存中的一个变量的值传递到主内存,以便随后的write操作使用
        + write(写入): 作用于主内存的变量,它把store操作从工作内存中得到的变量的值放入主内存的变量中
        > 如果要把一个变量从工作内存同步回主内存,必须要按顺序执行store和write操作,但不要求连续执行,可以插入其他指令
    * JMM关于同步的约定
        1. unlock前,必须将自己工作内存中的共享变量立即刷回主存
        2. lock前,必须读取主存中共享变量的最新值到工作内存中
        3. 加锁和解锁是同一把锁
        4. 不允许read和load,store和write操作之一单独出现
        
17. volatile关键字
    + volatile 是Java虚拟机轻量级的同步机制,重量级是Lock和Synchronized
    + 保证多线程环境下变量的相互可见性和有序性,但不保证原子性
        1. 使用volatile关键字会强制将修改的值立即写入主存
        2. 使用volatile关键字的话,当某线程将工作内存(缓存)中的数据写入主存时会将其他线程关于该变量的缓存行置为无效状态
        因此当其他线程需要读取这个变量时,发现自己缓存中关于该变量的缓存行是无效的,那么它就会从内存重新读取 -- 类似Intel的MESI缓存一致性协议和总线的嗅探机制
        3. 禁止进行指令重排序
    + 加了volatile关键字的编译成汇编码会多出一个lock前缀指令(CPU指令-内存屏障)
    + 使用volatile关键字的场景 --- 保证操作是原子性操作
        1. 对变量的写操作不依赖于当前值
        2. 该变量没有包含在具有其他变量的不变式中

18. 深入单例模式
    * 饿汉式 --- 编译期就将对象创建出来
        + 优点: 可以抑制反射,多线程环境下安全
        + 缺点: 资源耗费不合理
    * 懒汉式 --- 按需要再创建对象
        + 优点: 资源耗费合理
        + 缺点: 无法抑制反射,多线程环境下不安全
        DCL懒汉式 --- 双重检测锁
            + 优点: 多线程环境下安全
            + 缺点: 无法抑制反射
    * 枚举式 --- 利用枚举天生自带单例属性
        + 优点: 多线程环境下安全,可以抑制反射
19. 深入理解CAS(Compare And Swap)
    * 什么是CAS --- CPU的一种原语指令,属于乐观锁
        + 乐观锁: 每次不加锁而是假设没有冲突而去完成某项操作,如果因为冲突失败就重试,直到成功为止
        + 悲观锁: 这种线程一旦得到锁，其他需要锁的线程就挂起的情况,比如synchronized需切换线程状态
    * Unsafe类的作用 --- 用于Java间接操作内存,方法大多是native(调用C++操作内存)
        + Unsafe类中CAS的实现思想,如下:
        ```
            //自旋锁,死循环直到while条件成立
            public final native boolean compareAndSwapObject
                (Object var1, long var2, Object var4, Object var5){
                do{
                    value的地址值 = 对象var1地址值 + value在对象var1中地址相对偏移量var2;
                }while(判断value地址值中的值与var4的值是否相等,如果相等那么修改成var5的值);
            }
            优点: 无需切换线程状态,比如让某个线程阻塞/中断,乐观锁的好处
            缺点: 1.循环耗费CPU资源,但问题不大(CPU快)
                 2.只能保证一个共享变量的原子性,不能保证代码块的原子性
                 3.存在ABA问题
        ```
    * ABA问题(狸猫换太子) -- 线程A将元素修改成新的值,线程B又将元素修改回原来的值,但线程A并不知道
        + 解决方法: atomic原子引用 --- 带版本号的CAS
            + 每次有线程修改了值,就会导致版本号迭代
            + mysql中SQL提交也是如此实现的乐观锁
20. 各种锁的理解
    * 排他锁和共享锁
    * 乐观锁和悲观锁
    * 公平锁和非公平锁
        + 公平锁: 十分公平,必须队列排队执行,分先来后到
        + 非公平锁: 十分不公平,可以插队执行,效率更高
    * ReentrantLock可重入锁/递归锁
    ```
        /*
        *  可重入锁构造函数 默认创建的是非公平锁
        */
        public ReentrantLock(boolean fair) {
            sync = fair ? new FairSync() : new NonfairSync();
        }
        
        //思想: 指的是在同一线程内,外层函数获得锁之后,内层递归函数仍然可以获取到该锁
               并且同一个线程再次进入同步代码时,可以使用自己已获取到的锁
    ```
    * 自旋锁
    * 死锁分析
        1. jps -l 打印存活的进程号
        2. jstack pid 查看进程详情
21. AQS(Abstract Queue synchronized)
    * 通过 volatile state = (0|1) 标识当前是否有线程获取锁
    * 其中维护着FIFO的双向同步队列和FIFO的单向同步队列
    * 提供模板方法 acquire() 和 release() 给实现类重写决定如何获取和释放同步状态
    * 实现分为独占式同步状态和共享式同步状态
        + 独占式同步状态
          当线程获取同步状态失败后会被加入到CLH同步队列队尾并一直保持自旋,在CLH同步队列中
          的线程会在自旋时候判断其前驱节点是否为队列首节点,如果前驱为首节点就会不停的尝试获
          取同步状态,直到获取成功退出CLH同步队列,当线程执行完毕后会释放同步状态并唤醒后继.
        + 共享式同步状态
          