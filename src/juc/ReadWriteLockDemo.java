package juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @see java.util.concurrent.locks.ReadWriteLock
 * 维护了一对相关的锁: 读锁和写锁
 * 读和写是互斥的,写和写也是互斥的,读和读却允许
 * 读锁/共享锁: 以由多个 reader 线程同时保持共享数据
 * 写锁/独占锁: 只有一个线程 writer 线程可以修改共享数据
 */
public class ReadWriteLockDemo {

    public void test() {
        Resource bank = new Resource();
        for (int i = 1; i <= 50; i++) {
            new Thread(()->{
                bank.deposit(Thread.currentThread().getName(),((int) (Math.random() * 100)));
            },"Person_"+i).start();
        }
        for (int i = 1; i <= 50; i++) {
            new Thread(()->{
                bank.withdraw(Thread.currentThread().getName());
            },"Person_"+i).start();
        }
    }

    private static class Resource{
        private final Map<String,Integer> coffers = new HashMap<>();

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        public void deposit(String user,Integer money) {
            //写锁一上,读锁阻塞不允许读
            readWriteLock.writeLock().lock();
            try {
                System.out.println("客户:" + user + "正在存钱...");
                coffers.put(user,money);
                System.out.println("客户:" + user + "存钱￥"+money+"完毕!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

        public void withdraw(String user) {
            //读锁是为了允许别人一起读,写锁阻塞不允许写
            readWriteLock.readLock().lock();
            try {
                System.out.println(user + "正在取钱...");

                System.out.println(user + "取钱￥"+coffers.get(user)+"完毕!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }
}
