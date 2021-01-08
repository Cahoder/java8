package algorithm.structure.liner.queue;

import java.util.LinkedList;

/**
 * 链表双端队列
 * Double Ended Queue
 * @see java.util.Deque
 * @see java.util.LinkedList implements java.util.Deque
 * @param <E>
 */
public class Deque<E> {
    private LinkedList<E> storage = new LinkedList<>();
    public synchronized void enQueueRear(E E){ storage.addLast(E); }
    public synchronized E deQueueFront(){ return storage.removeFirst(); }
    public synchronized void enQueueFront(E E){ storage.addFirst(E); }
    public synchronized E deQueueRear(){ return storage.removeLast(); }
    public synchronized boolean empty(){ return storage.isEmpty(); }
    public synchronized E front(){ return storage.getFirst(); }
    public synchronized E rear(){ return storage.getLast(); }
    public synchronized String toString(){ return storage.toString(); }
    public synchronized void clear(){ storage = new LinkedList<>(); }
    public synchronized int size() { return storage.size(); }
}
