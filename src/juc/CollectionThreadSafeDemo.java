package juc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类为了效率默认是非线程安全
 * @see java.util.List
 * @see java.util.Set
 * @see java.util.Map
 */
public class CollectionThreadSafeDemo {
    private Collection<String> collection;

    public void ConcurrentModifyTest(Collection<String> _collection){
        collection = _collection;
        /*
         *  Exception in thread "Thread_7" java.util.ConcurrentModificationException
         * 	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:909)
         * 	at java.util.ArrayList$Itr.next(ArrayList.java:859)
         * 	at java.util.AbstractCollection.toString(AbstractCollection.java:461)
         * 	at java.lang.String.valueOf(String.java:2994)
         * 	at java.io.PrintStream.println(PrintStream.java:821)
         * 	at juc.CollectionThreadSafe.lambda$ConcurrentModifyTest$0(CollectionThreadSafe.java:26)
         * 	at java.lang.Thread.run(Thread.java:748)
         */
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                collection.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(collection);
            },"Thread_" + i).start();
        }
    }

    //解决方法一: Vector HashTable
    public <T extends Vector<String>> void ConcurrentModifySolve_1(T _collection) {
        collection = _collection;

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                collection.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(collection);
            },"Thread_" + i).start();
        }
    }

    //解决方法二: Collections.synchronizedXXX
    public <T extends Collection<String>> void ConcurrentModifySolve_2(T _collection) {
        collection = _collection;

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                collection.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(collection);
            },"Thread_" + i).start();
        }
    }

    //解决方法三: 使用并发包下的线程安全集合类
    /*
        CopyOnWriteArrayList 和 CopyOnWriteArraySet
        Copy-On-Write 即"写时复制",简称COW,是计算机程序设计领域中的一种优化策略
        基本思路: 从一开始大家都在共享同一个内容,读也是读这个内容
                但当有人想往一个容器添加元素的时候,不直接往当前容器添加,
                而是先将当前容器进行Copy,复制出一个新的容器,然后新的容器里添加元素,
                添加完元素之后，再将原容器的引用指向新的容器
        优点: 可以对CopyOnWrite容器进行并发的读而不需要加锁,因为当前容器不会添加任何元素
        所以CopyOnWrite容器也是一种读写分离的思想,即读和写不同的容器

        队列相关并发类: ArrayBlockingQueue ConcurrentLinkedDeque ConcurrentLinkedQueue

        映射相关并发类: ConcurrentHashMap -- 分段锁思想,跟HashTable一样不允许插入null
        ConcurrentSkipListMap
        ConcurrentSkipListSet
     */
    public void ConcurrentModifySolve_3(CopyOnWriteArrayList<String> _collection) {
        collection = _collection;

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                collection.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(Thread.currentThread().getName() + " : " + collection);
            },"Thread_" + i).start();
        }
    }
}
