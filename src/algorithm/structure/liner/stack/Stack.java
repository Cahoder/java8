package algorithm.structure.liner.stack;
import java.util.LinkedList;

/**
 * 链表栈
 * @see java.util.Stack extends java.util.Vector( synchronized --> Thread safe )
 * @see java.util.Vector extends AbstractList implements List
 * @param <E>
 */
public class Stack<E> {
    private LinkedList<E> storage = new LinkedList<>();  //使用链表数组储存
    public synchronized void push(E obj){ storage.addFirst(obj); }
    public synchronized E peek(){ return storage.getFirst(); }
    public synchronized E pop(){ return storage.removeFirst(); }
    public synchronized boolean empty(){ return storage.isEmpty(); }
    public synchronized String toString(){ return storage.toString(); }
    public synchronized void clear(){ storage = new LinkedList<>(); }
}