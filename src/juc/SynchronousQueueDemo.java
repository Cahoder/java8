package juc;

import java.util.concurrent.SynchronousQueue;

/**
 * 同步队列
 * @see java.util.concurrent.SynchronousQueue
 */
public class SynchronousQueueDemo {
    public void test(){
        SynchronousQueue<String> queue = new SynchronousQueue<>();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + " PUT " + 99);
                queue.put(String.valueOf(99));

                System.out.println(Thread.currentThread().getName() + " PUT " + 100);
                queue.put(String.valueOf(100));

                System.out.println(Thread.currentThread().getName() + " PUT " + 101);
                queue.put(String.valueOf(101));
            } catch (InterruptedException ignored) {}
        },"PUT_THREAD").start();


        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + " TAKE " + queue.take());
                System.out.println(Thread.currentThread().getName() + " TAKE " + queue.take());
                System.out.println(Thread.currentThread().getName() + " TAKE " + queue.take());
            } catch (InterruptedException ignored) {}
        },"TAKE_THREAD").start();
    }
}
