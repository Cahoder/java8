package algorithm.structure.liner.queue;

import algorithm.structure.liner.list.ArrayListPro;

/**
 * 循环队列
 * Queue Implements By Circle Array
 * @see algorithm.structure.liner.list.ArrayListPro
 * @param <E>
 */
public class QueueCircle<E> {
    /**
     * Dynamic Circle Array
     */
    ArrayListPro<E> storage = new ArrayListPro<>();

    /**
     * @param e 尾部入队
     */
    public synchronized void enQueue(E e){ storage.add(e); }

    /**
     * @return 头部出队
     */
    public synchronized E deQueue(){ return storage.remove(0); }

    /**
     * @return 获取头部
     */
    public synchronized E front(){ return storage.get(0); }

    /**
     * @return 队列是否为空
     */
    public synchronized boolean empty(){ return storage.isEmpty(); }

    /**
     * @return 打印队列元素
     */
    public synchronized String toString(){ return storage.toString(); }

    /**
     * 清空队列
     */
    public synchronized void clear(){ storage.clear(); }

    /**
     * @return 队列长度
     */
    public synchronized int size() { return storage.size(); }
}
