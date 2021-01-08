package juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @see java.util.concurrent.CyclicBarrier
 * 简单理解它是一个加法计数器
 * 它是通过await方法来计数,如果await之后未达到执行值
 * 则本条线程就会被阻塞,直到计数器累计到执行值
 * 如果CyclicBarrier构造方法传了Runnable参数,
 * 还要等Runnable里面run方法执行完毕后才会解除阻塞
 */
public class CyclicBarrierDemo {

    public void test(){
        CyclicBarrier barrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("--------保安关门--------");
            }
        });

        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "_Go Out!");
                try {
                    barrier.await(); //此条线程从这里开始阻塞
                    System.out.println("oh yeah");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"People_" + i).start();
        }
    }

}
