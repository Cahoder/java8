package structure;
import java.util.LinkedList;

//链表队列
public class MyQueue<T> {
    private LinkedList<T> storage = new LinkedList<>();  //使用链表数组储存
    public synchronized void enQueue(T obj){ storage.addLast(obj); }
    public synchronized T deQueue(){ return storage.removeFirst(); }
    public synchronized T front(){ return storage.getFirst(); }
    public synchronized boolean empty(){ return storage.isEmpty(); }
    public synchronized String toString(){ return storage.toString(); }
    public synchronized void clear(){ storage = new LinkedList<>(); }
}