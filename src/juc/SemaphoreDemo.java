package juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @see java.util.concurrent.Semaphore
 *
 * 它是指定时间内只能够允许一定数量的线程进入
 * 当数量不足时,其他线程只能在等待
 *
 * 类似于操作系统中的信号量: 一种可以动态定义的软件资源
 * typedef struct semaphore{
 *     int value;  //表示信号量,表示资源可复用次数
 *     struct pcb *list;    //当资源可复用次数小于等于0时,进入信号量等待进程队列
 * }
 */
public class SemaphoreDemo {

    public void test(){
        //public Semaphore(int permits, boolean fair) {}
        //构造函数默认使用非公平锁,可以插队的
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "_Go In!");
                    System.out.println("车位剩余: " + semaphore.drainPermits());
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + "_Go Out!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            },"Car_" + i).start();
        }
    }

}
