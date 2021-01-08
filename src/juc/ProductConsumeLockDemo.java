package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用并发包下的锁解决生产者消费者问题
 * @see java.util.concurrent.locks.Lock
 */
public class ProductConsumeLockDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    int size = 1;

    public void add() {
        lock.lock();
        try {
            while (size > 0) {
                condition.await();
            }

            size++;
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void remove() {
        lock.lock();
        try {
            while (size <= 0) {
                condition.await();
            }

            size--;
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private final Condition one = lock.newCondition();
    private final Condition two = lock.newCondition();
    private final Condition three = lock.newCondition();
    private final Condition four = lock.newCondition();
    /**
     * 通过 lock.newCondition()
     * 实现精准控制,先干什么后干什么
     */
    public void one() {
        lock.lock();
        try {
            while (size!=1) {
                one.await();
            }
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            size = 2;
            //告诉two执行任务啦
            two.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void two() {
        lock.lock();
        try {
            while (size!=2) {
                two.await();
            }
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            size = 3;
            //告诉three执行任务啦
            three.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void three() {
        lock.lock();
        try {
            while (size!=3) {
                three.await();
            }
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            size = 4;
            //告诉four执行任务啦
            four.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void four() {
        lock.lock();
        try {
            while (size!=4) {
                four.await();
            }
            System.out.println(Thread.currentThread().getName() + " size : " + size);
            size = 1;
            //告诉one执行任务啦
            one.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
