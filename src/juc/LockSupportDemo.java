package juc;

import java.util.concurrent.locks.LockSupport;

/**
 * @see java.util.concurrent.locks.LockSupport
 * 当调用LockSupport.park时，表示当前线程将会等待，直至获得许可，
 * 当调用LockSupport.unpark时，必须把等待获得许可的线程作为参数进行传递，好让此线程继续运行
 */
public class LockSupportDemo {

    private static class WaitNotifySync implements Runnable {

        private WaitNotifySync() {}

        private static final WaitNotifySync sync = new WaitNotifySync();

        @Override
        public void run() {
            synchronized (sync) {
                System.out.println("thread before notify");
                sync.notify();
                System.out.println("thread after notify");
            }
        }

        public static void main(String[] args) throws InterruptedException {
            //wait()必须先与notify()调用
            invokeWaitThenNotify();
            //notify()先于wait()调用会导致程序异常无法退出
            //invokeNotifyThenWait();
        }

        @SuppressWarnings("all")
        private static void invokeWaitThenNotify() throws InterruptedException {
            synchronized (sync) {
                Thread thread = new Thread(sync);
                thread.start();
                System.out.println("main sleep 3s ...");
                Thread.sleep(3000);
                System.out.println("main before wait");
                sync.wait();
                System.out.println("main after wait");
            }
        }

        @SuppressWarnings("all")
        private static void invokeNotifyThenWait() throws InterruptedException {
            Thread thread = new Thread(sync);
            thread.start();
            System.out.println("main sleep 3s ...");
            Thread.sleep(3000);
            synchronized (sync) {
                System.out.println("main before wait");
                sync.wait();
                System.out.println("main after wait");
            }
        }

    }

    private static class ParkUnParkSync extends Thread {

        private final Object sync;

        public ParkUnParkSync(Object sync) {
            this.sync = sync;
        }

        @Override
        @SuppressWarnings("all")
        public void run() {
            System.out.println("thread before unpark");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 获取主线程blocker
            System.out.println("thread blocker info : " + LockSupport.getBlocker((Thread) sync));

            // 调用unPark()或发生中断 均会释放主线程许可
            LockSupport.unpark((Thread) sync);
            //((Thread) sync).interrupt();

            // 休眠500ms，保证先执行main线程park()中的setBlocker(t, null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 此时获取主线程blocker为null
            System.out.println("thread blocker info : " + LockSupport.getBlocker((Thread) sync));
            System.out.println("thread after unpark");
        }

        public static void main(String[] args) {
            //unPark()和park()同步不受调用顺序影响
            //invokeParkThenUnPark();
            invokeUnParkThenPark();
        }

        @SuppressWarnings("all")
        private static void invokeParkThenUnPark() {
            Thread thread = new ParkUnParkSync(Thread.currentThread());
            thread.start();
            System.out.println("main before park");
            // 阻塞主线程许可
            LockSupport.park("i am main blocker");
            System.out.println("main after park");
        }

        @SuppressWarnings("all")
        private static void invokeUnParkThenPark() {
            Thread thread = new ParkUnParkSync(Thread.currentThread());
            thread.start();
            try {
                System.out.println("main sleep 1s ...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main before park");
            // 阻塞主线程许可
            LockSupport.park("i am main blocker");
            System.out.println("main after park");
        }

    }

}