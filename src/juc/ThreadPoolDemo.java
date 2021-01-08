package juc;

import java.util.concurrent.*;

/**
 * 线程池
 * @see java.util.concurrent.Executors
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see java.util.concurrent.RejectedExecutionHandler
 */
public class ThreadPoolDemo {

    //线程池的三大创建方法,不推荐:它们的maximumPoolSize给的太大了,会OOM
    public void test1(){
//        ExecutorService pool = Executors.newSingleThreadExecutor();
//        ExecutorService pool = Executors.newFixedThreadPool(20);
        ExecutorService pool = Executors.newCachedThreadPool();

        try {

            for (int i = 0; i < 20; i++) {
                pool.execute(()->{
                    System.out.println(Thread.currentThread().getName());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    //使用底层的ThreadPoolExecutor七大参数构建线程池,推荐这种方法
    public void test2(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,5,
                3L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

        try {
            // 小于等于5个任务,不会触发maximumPollSize策略
            // 大于5个任务触发maximumPollSize策略,再创建3个线程
            // 当8个任务时候,此时maximumPollSize和LinkedBlockingQueue都满了,进入最大并发数,再添加就会触发拒绝执行策略
            // java.util.concurrent.RejectedExecutionException: Task juc.ThreadPoolDemo$$Lambda$1/1480010240@7ef20235 rejected
            // [Running, pool size = 5, active threads = 5, queued tasks = 3, completed tasks = 0]
            for (int i = 1; i <= 9; i++) {
                pool.execute(()->{
                    System.out.println(Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException ignored) {}
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    //四种拒绝执行策略
    public void test3(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1,1,
                3L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1),
                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy() //忽视它并抛出异常
//                new ThreadPoolExecutor.CallerRunsPolicy() //让调用者处理
//                new ThreadPoolExecutor.DiscardPolicy() //忽视它但不抛出异常,它后续的任务也不要了
                new ThreadPoolExecutor.DiscardOldestPolicy() //抛弃队列中最早入队的并替代它
        );
        try {
            for (int i = 1; i <= 4; i++) {
                pool.execute(()->{
                    System.out.println(Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException ignored) {}
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}
