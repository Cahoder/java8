package algorithm.structure.liner.queue;

import algorithm.structure.liner.list.ArrayListPro;

/**
 * 双端循环队列
 * Deque Implements By Circle Array
 * @see algorithm.structure.liner.list.ArrayListPro
 * @param <E>
 */
public class DequeCircle<E> {
    /**
     * Dynamic Circle Array
     */
    private final ArrayListPro<E> storage = new ArrayListPro<>();

    /**
     * @param e 尾部入队
     */
    public synchronized void enQueueRear(E e){ storage.add(e); }

    /**
     * @return 头部出队
     */
    public synchronized E deQueueFront(){ return storage.remove(0); }

    /**
     * @param e 头部入队
     */
    public synchronized void enQueueFront(E e){ storage.add(0,e); }

    /**
     * @return 尾部出队
     */
    public synchronized E deQueueRear(){ return storage.remove(storage.size()-1); }

    /**
     * @return 队列是否为空
     */
    public synchronized boolean empty(){ return storage.isEmpty(); }

    /**
     * @return 获取头部
     */
    public synchronized E front(){ return storage.get(0); }

    /**
     * @return 获取尾部
     */
    public synchronized E rear(){ return storage.get(storage.size()-1); }

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
