package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS --- CPU的一种原语指令
 * CompareAndSwap对比并交换
 * @see java.util.concurrent.atomic
 */
public class CASDemo {

    public void test1(){
        AtomicInteger integer = new AtomicInteger(2019);

        /*
            // setup to use Unsafe.compareAndSwapInt for updates
            private static final Unsafe unsafe = Unsafe.getUnsafe();
            private static final long valueOffset;      //相对寻址方式,这是对象成员value相对于对象地址的偏移量

            static {
                try {
                    valueOffset = unsafe.objectFieldOffset
                        (AtomicInteger.class.getDeclaredField("value"));
                } catch (Exception ex) { throw new Error(ex); }
            }

            //如果integer对象的期望值是我想要的,那我就把他修改成新的值
            public final boolean compareAndSet(int expect, int update) {
                return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
            }

            自旋锁: 死循环判断如下规则
            对象var1地址值 + value在对象var1中地址相对偏移量var2 = value的地址值,
            判断value地址值中的值与var4的值是否相等,如果相等那么修改成var5的值
            public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
        */
        System.out.println(integer.compareAndSet(2019, 2020));  //true
        System.out.println(integer.compareAndSet(2019, 2020));  //false
    }

    /*
        但是普通的CAS操作会出现ABA问题
        ABA问题: 线程A将元素修改成新的值,线程B又将元素修改回原来的值,但线程A并不知道
        解决方法: 原子引用 -- - 带版本号的cas
     */
    public void test2(){
        //AtomicInteger 对整数进行原子操作,如果是一个POJO呢?可以用 AtomicReference 来包装这个POJO,使其操作原子化
        AtomicReference<CASDemo> atomicRefer = new AtomicReference<>();
        CASDemo caseD1 = new CASDemo();
        CASDemo caseD2 = new CASDemo();
        System.out.println(caseD1 + "-" + caseD2);
        atomicRefer.set(caseD1);
        System.out.println(atomicRefer.compareAndSet(caseD1,caseD2)+"\t"+atomicRefer.get()); // true,此时原子引用改成了caseD2
        System.out.println(atomicRefer.compareAndSet(caseD1,caseD2)+"\t"+atomicRefer.get()); // false

        //AtomicReference<Integer> atomicRefer = new AtomicReference<>(2020);
        //注意Integer包装类,在-128和127之间的赋值,Integer对象是在IntegerCache.cache产生
        //会复用已有的对象,因此compareAndSet底层使用==进行比较会出问题,推荐使用equals进行判断
        final AtomicStampedReference<Integer> atomStampRefer = new AtomicStampedReference<>(1,1);

        new Thread(()->{
            /*
             * expectedReference, 预期值引用
             * newReference, 新值引用
             * expectedStamp, 预期值时间戳
             * newStamp, 新值时间戳
             */
            boolean a = atomStampRefer.compareAndSet(1, 2, 1, 2);
            System.out.println(Thread.currentThread().getName() + " : " + a);
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getStamp());
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getReference());

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            a = atomStampRefer.compareAndSet(2,3,2,3);
            System.out.println(Thread.currentThread().getName() + " : " + a);
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getStamp());
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getReference());

        },"A").start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean b = atomStampRefer.compareAndSet(2, 1, 1, 2);
            System.out.println(Thread.currentThread().getName() + " : " + b);
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getStamp());
            System.out.println(Thread.currentThread().getName() + " : " + atomStampRefer.getReference());

        },"B").start();
    }
}
