package algorithm.structure.liner.queue;
import java.util.LinkedList;

/**
 * 链表队列
 * @see java.util.Queue
 * @see java.util.LinkedList implements java.util.Queue
 * @param <E>
 */
public class Queue<E> {
    private LinkedList<E> storage = new LinkedList<>();
    public synchronized void enQueue(E e){ storage.addLast(e); }
    public synchronized E deQueue(){ return storage.removeFirst(); }
    public synchronized E front(){ return storage.getFirst(); }
    public synchronized boolean empty(){ return storage.isEmpty(); }
    public synchronized String toString(){ return storage.toString(); }
    public synchronized void clear(){ storage = new LinkedList<>(); }
    public synchronized int size() { return storage.size(); }
}