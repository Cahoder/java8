package juc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列的四种API调用区别
 * @see java.util.concurrent.BlockingQueue
 */
public class BlockingQueueDemo<E> {

    private final BlockingQueue<E> queue;

    public BlockingQueueDemo(BlockingQueue<E> _queue) {
        queue = _queue;
    }

    /*
        添加失败或删除失败都会抛出异常
     */
    public void test1(E...e){
        System.out.println("添加: ");
        for (int i = 0; i < e.length; i++)
            System.out.println(queue.add(e[i]));
        System.out.println("删除: ");
        for (int i = 0; i < e.length; i++)
            System.out.println(queue.remove());
    }

    /*
        获取队元素失败会抛出异常
     */
    public E front1(E e){
        System.out.println("获取元素:");
        return queue.element();
    }

    /*
        添加失败或删除失败异常会被返回值替代
     */
    public void test2(E...e){
        System.out.println("添加: ");
        for (int i = 0; i < e.length; i++)
            System.out.println(queue.offer(e[i]));
        System.out.println("删除: ");
        for (int i = 0; i < e.length; i++)
            System.out.println(queue.poll());
    }

    /*
        获取队元素失败异常会被返回值替代
     */
    public E front2(E e){
        System.out.println("获取元素:");
        return queue.peek();
    }

    /*
        添加失败或删除失败会将线程阻塞直到可以添加或删除
     */
    public void test3(E...e) throws InterruptedException {
        System.out.println("添加: ");
        for (int i = 0; i < e.length; i++)
            queue.put(e[i]);
        System.out.println("删除: ");
        for (int i = 0; i < e.length; i++)
            System.out.println(queue.take());
    }

    /*
        添加失败或删除失败会将线程阻塞等待一段时间,超过这段时间还是失败则放弃
     */
    public void test4(long timeout, TimeUnit unit,E...e) throws InterruptedException {
        System.out.println("添加: ");
        for (int i = 0; i < e.length; i++){
            if (queue.remainingCapacity() == 0) System.out.println("waiting "+timeout+" seconds...");
            System.out.println(queue.offer(e[i], timeout, unit));
        }
        System.out.println("删除: ");
        for (int i = 0; i < e.length; i++){
            if (queue.size() == 0) System.out.println("waiting "+timeout+" seconds...");
            System.out.println(queue.poll(timeout, unit));
        }
    }
}
