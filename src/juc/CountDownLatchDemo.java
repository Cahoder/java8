package juc;

import java.util.concurrent.CountDownLatch;

/**
 * @see java.util.concurrent.CountDownLatch
 * 简单理解它是一个减法计数器
 * 在CountDownLatch归零之前,await方法后的代码都会被阻塞
 */
public class CountDownLatchDemo {

    //小学生放学,保安必须等小学生走光了才能关门
    public void test() {
        CountDownLatch count = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "_Go Out!");
                System.out.println("oh yeah");
                count.countDown();
            },"People_" + i).start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--------保安关门--------");
    }
}
