package structure;
import java.util.*;

//链表栈
public class MyStack<T> {
    private LinkedList<T> storage = new LinkedList<>();  //使用链表数组储存
    public synchronized void push(T obj){ storage.addFirst(obj); }
    public synchronized T peek(){ return storage.getFirst(); }
    public synchronized T pop(){ return storage.removeFirst(); }
    public synchronized boolean empty(){ return storage.isEmpty(); }
    public synchronized String toString(){ return storage.toString(); }
    public synchronized void clear(){ storage = new LinkedList<>(); }
}
