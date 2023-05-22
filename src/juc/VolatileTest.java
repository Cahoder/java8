package juc;

/**
 * volatile可以保证可见性
 * 但volatile变量的运算在并发下非线程安全
 **/
public class VolatileTest {

    public static volatile int race = 0;
    public static void increase() {
        race++;
    }
    private static final int THREADS_COUNT = 20;
    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    increase();
                }
            });
            threads[i].start();
        }
        // 等待所有累加线程都结束(debug模式下调用才有用)
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(race);
    }

}
