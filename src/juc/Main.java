package juc;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
//        productConsumeByJUCTest();
//        CollectionTest();
//        CountDownLatchTest();
//        CyclicBarrierTest();
//        SemaphoreTest();
//        ReadWriteLockTest();
//        BlockingQueueApiTest();
//        SynchronousQueueTest();
//        ThreadPoolTest();
//        ForkJoinTest();
//        FutureTest();
//        CASTest();
        SpinLockTest();
    }

    private static void SpinLockTest() {
        SpinLock lock = new SpinLock();
        new Thread(()->{
            lock.lock();
            System.out.println("locking1");
//            lock.unlock();  //加锁和解锁不匹配,会死锁
        }).start();
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            System.out.println("locking2");
            lock.unlock();
        }).start();
    }

    private static void CASTest(){
        CASDemo demo = new CASDemo();
//        demo.test1();
        demo.test2();
    }

    private static void FutureTest() {
        FutureDemo futureDemo = new FutureDemo();
        futureDemo.test1();
        System.out.println("-----------------------");
        System.out.println(futureDemo.test2());
    }

    private static void ForkJoinTest() {
        ForkJoinDemo forkJoinDemo = new ForkJoinDemo();
        // 0 1 1 2 3 5 8 13 21 34 55 89 144 ……
        System.out.println(forkJoinDemo.test_forkjoin_1(40));
        System.out.println(forkJoinDemo.test_stream_parallel(40));
        //分支打印
        forkJoinDemo.test_forkjoin_2(1_00);
    }

    private static void ThreadPoolTest() {
        ThreadPoolDemo poolDemo = new ThreadPoolDemo();
//        poolDemo.test1();    //三大创建方式
//        poolDemo.test2();   //七个构建参数
        poolDemo.test3();   //四种拒绝执行策略
    }

    private static void SynchronousQueueTest() {
        SynchronousQueueDemo demo = new SynchronousQueueDemo();
        demo.test();
    }

    private static void BlockingQueueApiTest() throws InterruptedException {
//        BlockingQueueDemo<String> queueDemo = new BlockingQueueDemo<>(new ArrayBlockingQueue<>(3));
        BlockingQueueDemo<String> queueDemo = new BlockingQueueDemo<>(new LinkedBlockingDeque<>(3));
        String[] strings_1 = {"a","b","c"};
        String[] strings_2 = {"a","b","c","d"};
        System.out.println("=====================add|remove|element=====================");
        queueDemo.test1(strings_1);
//        System.out.println(queueDemo.front1("d"));  //Exception in thread "main" java.util.NoSuchElementException
//        queueDemo.test1(strings_2); //Exception in thread "main" java.lang.IllegalStateException: Queue full

        System.out.println("=====================offer|poll|peek=====================");
        queueDemo.test2(strings_1);
        System.out.println(queueDemo.front2("d"));  //null
        queueDemo.test2(strings_2);  //true true true false

        System.out.println("=====================Block(put|take)=====================");
        queueDemo.test3(strings_1);
//        queueDemo.test3(strings_2);   //thread blocking...

        System.out.println("=====================TimeWait(offer|poll)=====================");
        queueDemo.test4(2,TimeUnit.SECONDS,strings_1);
        queueDemo.test4(2,TimeUnit.SECONDS,strings_2);
    }

    private static void ReadWriteLockTest() {
        ReadWriteLockDemo readWriteLock = new ReadWriteLockDemo();
        readWriteLock.test();
    }

    private static void SemaphoreTest() {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        semaphoreDemo.test();
    }

    private static void CyclicBarrierTest() {
        CyclicBarrierDemo barrierDemo = new CyclicBarrierDemo();
        barrierDemo.test();
    }

    private static void CountDownLatchTest() {
        CountDownLatchDemo latchDemo = new CountDownLatchDemo();
        latchDemo.test();
    }


    public static void CollectionTest(){
        CollectionThreadSafeDemo col = new CollectionThreadSafeDemo();
        col.ConcurrentModifyTest(new ArrayList<>());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ignored) {}
        System.out.println("___________________________Solve_method_1_Vector___________________________");
        col.ConcurrentModifySolve_1(new Vector<>());
        System.out.println("___________________________Solve_method_2_Collections.synchronizedXXX___________________________");
        col.ConcurrentModifySolve_2(Collections.synchronizedList(new ArrayList<>()));
        System.out.println("___________________________Solve_method_3_JUC___________________________");
        col.ConcurrentModifySolve_3(new CopyOnWriteArrayList<>());
    }

    public static void productConsumeByJUCTest(){
        ProductConsumeLockDemo data = new ProductConsumeLockDemo();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                //data.add();
                data.one();
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                //data.remove();
                data.two();
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                //data.add();
                data.three();
            }
        },"C").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                //data.remove();
                data.four();
            }
        },"D").start();
    }
}
