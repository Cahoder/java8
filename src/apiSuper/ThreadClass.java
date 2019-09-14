package apiSuper;
import foundation.MyRunnable;
import foundation.MyThread;

import java.lang.String;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println(Thread.currentThread().getName()+ "执行第"+(i+1)+ "次");
                }
            }
        };
        new Thread(r,"线程三").start();
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


    /**
     * 线程之间的通信：等待与唤醒机制
     * 案例：生产者生产包子，消费者消费包子
     *
     */
    public void threadWaitNotifyMechanism(){

        //包子集合对象
        HashMap<String,Object> baozi = new HashMap<>();
        baozi.put("baozi-pi","");
        baozi.put("baozi-xian","");
        baozi.put("baozi-num",0);
        baozi.put("baozi-flag",false);

        //生产者线程
        Runnable productor = () -> {
            while (true) synchronized (baozi) {
                //如果当前包子状态为有
                if (baozi.get("baozi-flag").equals(true)) {
                    try {
                        baozi.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //包子状态为没有||被唤醒后
                System.out.println("\n===========生产者开始生产===========");
                product(baozi);
                baozi.put("baozi-flag",true);
                baozi.notify();
            }
        };

        //消费者线程
        Runnable consumer = () -> {
            while (true) synchronized (baozi) {
                //如果当前包子状态为没有
                if (baozi.get("baozi-flag").equals(false)) {
                    try {
                        baozi.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //包子状态为有||被唤醒后
                System.out.println("===========消费者开始消费===========");
                consume(baozi);
                baozi.put("baozi-flag",false);
                baozi.notify();
            }
        };

        new Thread(productor).start();
        new Thread(consumer).start();

    }



    private void consume(HashMap<String,Object> baozi){
        if (!(baozi.containsKey("baozi-pi") || baozi.containsKey("baozi-xian") || baozi.containsKey("baozi-num") || baozi.containsKey("baozi-flag")))return;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(baozi.get("baozi-pi").toString()+baozi.get("baozi-xian").toString()+"包子被我吃掉了，包子铺快给我制作包子吧");
    }

    private void product(HashMap<String,Object> baozi){
        if (!(baozi.containsKey("baozi-pi") || baozi.containsKey("baozi-xian") || baozi.containsKey("baozi-num") || baozi.containsKey("baozi-flag")))return;

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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        baozi.put("baozi-num",++num);
        System.out.println(baozi.get("baozi-pi").toString()+baozi.get("baozi-xian").toString()+"包子制作好啦，吃货快来吃包子吧！");
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
