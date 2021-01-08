package apiSuper;
import foundation.MyRunnable;
import foundation.MyThread;

import java.lang.String;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//线程start()方法与run()方法的区别
//线程对象调用run()方法不开启线程，仅是对象调用方法(仅会进行单线程执行)
//线程对象调用start()方法开启线程，并让jvm调用run()方法在开启的线程中执行

public class ThreadClass {

    public void createThread(){
        MyRunnable mr = new MyRunnable(() -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("myThread:" + i);
            }
        });
        Thread subThread = new Thread(mr);
//        subThread.run();  //相当于单线程对象调用方法，绝对有序顺序执行
        subThread.start();  //抢占CPU执行权非一定有序顺序执行


        for (int i = 0; i < 20; i++) {
            System.out.println("MainThread:" + i);
        }
    }


    public void  sleepThread(){
        for (int i = 0; i < 60; i++) {
            System.out.println(i+1);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //匿名函数开启多线程方法
    public void anonymousThread(){
        //写法一
        //直接继承Thread类
        new Thread("线程一"){
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName()+ "执行第"+(i+1)+ "次");
                }
            }
        }.start();

        //写法二
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName()+ "执行第"+(i+1)+ "次");
                }
            }
        },"线程二").start();

        //写法三
        //实现Runnable接口
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName()+ "执行第"+(i+1)+ "次");
                }
            }
        };
        new Thread(r,"线程三").start();

        //写法四
        /*
         * 区别Runnable接口,它是有放回值并且效率比Runnable高
         * 1.需要指定泛型Callable<V>返回类型
         * 2.重写call方法,可以按需要抛出异常
         * 3.创建目标对象
         * 4.需要创建执行服务: ExecutorService service = Executors.newFixedThreadPool(int taskNumbers);
         * 5.提交执行: Future<V> result1 = service.submit(task1);
         * 6.获取结果: V result = result1.get() throws InterruptedException, ExecutionException;
         * 7.关闭服务: service.shutdownNow();
         */
        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName()+ "执行第"+(i+1)+ "次");
                }
                return true;
            }
        };
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<Object> taskResult = service.submit(task);
        try {
            //如果Future中的call()方法还在执行中,get()方法会被阻塞
            Object taskReturn = taskResult.get();
            System.out.println(taskReturn);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdownNow();

        //同样如果不想使用服务启动Callable接口也可以直接使用FutureTask
        //FutureTask implements RunnableFuture extends Runnable
        FutureTask<Object> futureTask = new FutureTask<>(task);
        //这里有两条线程都执行了futureTask,但是只会打印一次结果
        //FutureTask会对call方法返回值进行缓存,提高效率
        new Thread(futureTask,"ThreadFirst").start();
        new Thread(futureTask,"ThreadTwo").start();

    }


    //线程安全问题
    public void threadSecureProblem(){

        final int[] number = {100};
        Runnable r = () -> {
            while (number[0]>0){
                try {
                    Thread.sleep(10);    //放大容易出现线程安全问题的概率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
                number[0]--;
            }
        };

        new Thread(r,"Thread-0").start();
        new Thread(r,"Thread-1").start();
        new Thread(r,"Thread-2").start();

    }

    //-------------------线程同步方法start-------------------
    //性能对比:  显式锁java.util.concurrent.locks > 同步代码块 > 同步方法 > 静态同步方法
    /**
     * 线程安全解决方法一：同步代码块
     * 通过代码块中的锁对象（可以使用任意的对象）
     * 注意：
     *  必须保证多个线程使用的锁对象是同一个对象
     * 原理：
     *  通过锁对象保证每次只能够让一个线程在代码块中执行
     *
     */
    public void threadSecureSyncCodeBlock(){
        final int[] number = {100};
        Object lockObject = new Object();

        Runnable r = () -> {
            while (number[0]>0){
                synchronized (lockObject){
                    if (number[0]==0) return;
                    try {
                        Thread.sleep(10);    //放大容易出现线程安全问题的概率
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
                    number[0]--;
                }
            }
        };

        new Thread(r,"Thread-0").start();
        new Thread(r,"Thread-1").start();
        new Thread(r,"Thread-2").start();
    }

    /**
     * 线程安全解决方法二：同步方法
     * 原理和同步代码块相同
     * 锁对象默认为：方法调用者（this）
     */
    private synchronized void threadSecureSyncMethod(int[] number){

//        synchronized (this){
//            if (number[0]==0) return;
//            try {
//                Thread.sleep(10);    //放大容易出现线程安全问题的概率
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
//            number[0]--;
//        }

        if (number[0]==0) return;
        try {
            Thread.sleep(10);    //放大容易出现线程安全问题的概率
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
        number[0]--;
    }

    /**
     * 线程安全解决方法三：静态同步方法
     * 原理和同步代码块相同
     * 锁对象默认为：本类的java.lang.class对象（Heap中存储着）
     */
    private static synchronized void threadSecureSyncStaticMethod(int[] number){

//        synchronized (ThreadClass.class){
//            if (number[0]==0) return;
//            try {
//                Thread.sleep(10);    //放大容易出现线程安全问题的概率
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
//            number[0]--;
//        }

        if (number[0]==0) return;
        try {
            Thread.sleep(10);    //放大容易出现线程安全问题的概率
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
        number[0]--;
    }



    /**
     * 线程安全解决方法四：自己上Lock锁
     * 锁接口 java.util.concurrent.locks.lock
     *
     */
    public void threadSecureSyncLock(){
        final int[] number = {10};
        Lock lock = new ReentrantLock();
        Runnable r = () -> {
            while (number[0]>0){

                try {
                    lock.lock(); //上锁
                    if (number[0]==0) return;

                    Thread.sleep(10);    //放大容易出现线程安全问题的概率
                    System.out.println(Thread.currentThread().getName() + "--->操作数字" + number[0]);
                    number[0]--;
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();  //解锁
                }

            }
        };

        new Thread(r,"Thread-0").start();
        new Thread(r,"Thread-1").start();
        new Thread(r,"Thread-2").start();
    }
    //-------------------线程同步方法end-------------------


    //-------------------并发协作模型start-------------------
    /**
     * 线程之间的通信：等待与唤醒机制 -- 管程法
     * 案例：生产者生产包子，消费者消费包子
     *
     */
    public void threadWaitNotifyMechanism(){
        //产品：包子集合对象 --- 缓冲区
        HashMap<String,Object> baozi = new HashMap<>();
        baozi.put("baozi-pi","");
        baozi.put("baozi-xian","");
        baozi.put("baozi-num",0); //缓冲区

        //生产者线程
        Runnable productor = () -> {
            while (true) synchronized (baozi) {
                //如果当前包子状态为有
                if (baozi.get("baozi-num").equals(10)) {
                    try {
                        baozi.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //包子状态为没有||被唤醒后
                System.out.println("\n===========生产者开始生产===========");
                product(baozi);
                baozi.notify();
            }
        };

        //消费者线程
        Runnable consumer = () -> {
            while (true) synchronized (baozi) {
                //如果当前包子状态为没有
                if (baozi.get("baozi-num").equals(0)) {
                    try {
                        baozi.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //包子状态为有||被唤醒后
                System.out.println("\n===========消费者开始消费===========");
                consume(baozi);
                baozi.notify();
            }
        };

        new Thread(productor).start();
        new Thread(consumer).start();

    }
    private void consume(HashMap<String,Object> baozi){
        if (!(baozi.containsKey("baozi-pi") || baozi.containsKey("baozi-xian") || baozi.containsKey("baozi-num")))return;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer num = (Integer)baozi.get("baozi-num");
        baozi.put("baozi-num",--num);
        System.out.println(baozi.get("baozi-pi").toString()+baozi.get("baozi-xian").toString()+"包子被我吃掉了，包子铺快给我制作包子吧");
    }
    private void product(HashMap<String,Object> baozi){
        if (!(baozi.containsKey("baozi-pi") || baozi.containsKey("baozi-xian") || baozi.containsKey("baozi-num")))return;

        Integer num = (Integer)baozi.get("baozi-num");
        if (num %2 == 0){
            baozi.put("baozi-pi","A面皮");
            baozi.put("baozi-xian","韭菜蘑菇肉");
        }
        if (num %2 == 1){
            baozi.put("baozi-pi","B筋皮");
            baozi.put("baozi-xian","牛肉白菜花");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        baozi.put("baozi-num",++num);
        System.out.println(baozi.get("baozi-pi").toString()+baozi.get("baozi-xian").toString()+"包子制作好啦，吃货快来吃包子吧！");
    }

    /**
     * 线程之间的通信： 信号灯法
     * 案例：红绿灯
     */
    public void threadSignFlagMechanism(){
        //T green, F red
        Boolean[] flag = {false};

        //红灯停
        Runnable red = new Runnable() {
            @Override
            public void run() {
                while (true) synchronized (flag) {
                    //如果当前是绿灯
                    if (flag[0]) {
                        try {
                            flag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //如果当前是红灯
                    System.out.println("红灯停止过去...");
                    flag[0] = !flag[0];
                    flag.notify();
                }
            }
        };
        //绿灯行
        Runnable green = new Runnable() {
            @Override
            public void run() {
                while (true) synchronized (flag) {
                    //如果当前是红灯
                    if (!flag[0]) {
                        try {
                            flag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //如果当前是绿灯
                    System.out.println("绿灯可以过去...");
                    flag[0] = !flag[0];
                    flag.notify();
                }
            }
        };

        new Thread(green).start();
        new Thread(red).start();
    }
    //-------------------并发协作模型end-------------------

    /**
     * 守护进程与用户进程
     * jvm必须保证用户进程执行完毕
     * jvm不用等待守护进程执行完毕 --> 例如： 垃圾回收 日志记录 内存监控
     */
    public void daemonThread(){
        new Thread(() -> {
            for (int i = 0; i < 36500; i++) {
                System.out.println("今天是我活着的第"+i+"天");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("我去世了");
        },"UserThread").start();

        Thread daemonThread = new Thread(() -> {
            do {
                System.out.println("上帝永远保佑着你!");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }, "DaemonThread");
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    /**
     * 线程池的原理
     */
    public void threadCreatePool(int initPool){

        LinkedList<MyThread> threadPool = new LinkedList<>();   //线程池
        ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(initPool*4);   //任务队列
        /*创建任务队列*/
        for (int i = 0; i < initPool*4; i++) {
            taskQueue.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("大家好我是 " + Thread.currentThread().getName() + " 在执行任务代号"+this.hashCode()+"中......");
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        /*创建线程池*/
        for (int i = 0; i < initPool; i++) {
            MyThread thread = new MyThread();
            thread.start();
            threadPool.add(thread);
        }

        /*调取线程池的线程工作*/
        while (threadPool.size()>0 && taskQueue.size()>0){
            MyThread t = threadPool.remove();
            if (t.getTask() == null) {  //必须要有这个判断？？？
                t.setTask(taskQueue.poll());
            }
            threadPool.add(t);
        }

        //关闭线程池
        /*TODO*/

    }

    /**
     * JDK1.5 之后java封装了线程池类
     * */
    public void threadPool(int initPool){
        ExecutorService es = Executors.newFixedThreadPool(initPool);  //面向接口编程

        for (int i = 0; i < initPool*4; i++) {
            es.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (es){
                        try {
                            System.out.println("大家好我是 " + Thread.currentThread().getName() + " 在执行任务代号"+this.hashCode()+"中......");
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        es.shutdown();  //关闭线程池
    }
}
