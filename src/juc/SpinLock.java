package juc;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * @see sun.misc.Unsafe#compareAndSwapObject(Object, long, Object, Object)
 */
public class SpinLock {
    protected final AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock(){
        Thread current = Thread.currentThread();
        while (!sign.compareAndSet(null,current));
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        sign.compareAndSet(current,null);
    }
}

/**
 * 由自旋锁实现的可重入锁
 */
class ReetrantSpinLock extends SpinLock{
    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        if (current.equals(sign.get())) {
            count.getAndIncrement();
            return;
        }
        while (!sign.compareAndSet(null,current));
    }

    @Override
    public void unlock() {
        Thread current = Thread.currentThread();
        if (current.equals(sign.get())) {
            if (count.get() > 0){
                count.getAndDecrement();
            } else {
                sign.compareAndSet(current,null);
            }
        }
    }
}
