package algorithm.structure.heap;

import java.util.Comparator;

public abstract class AbstractHeap<E> implements Heap<E> {

    /**
     * Dynamic Array Size
     */
    protected int size;

    /**
     * A Comparator If you have new custom compare rule
     */
    protected final Comparator<E> comparator;

    /**
     * @param comparator Use Comparator rule if it's not null
     */
    protected AbstractHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * @return Heap Capacity
     */
    public int size() {
        return size;
    }

    /**
     * @return Check whether the Heap is Empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 确保堆中元素不为空
     */
    protected void emptyCheck() {
        if (isEmpty())
            throw new IndexOutOfBoundsException("Heap is Empty");
    }

    /**
     * @param element Element Argument Not Null Check
     */
    protected void elementNotNullCheck(E element) {
        if (element == null)
            throw new IllegalArgumentException("element must not be null !");
    }

    /**
     * @param e1 the first object to be compared.
     * @param e2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the second.
     */
    @SuppressWarnings("unchecked")
    protected int compare(E e1, E e2) {
        if (comparator != null) return comparator.compare(e1, e2);
        //Use the E default comparable rule
        //We assume that the E have implements the java.lang.comparable interface
        //if E not it will throw a runtime exception
        return ((Comparable<E>) e1).compareTo(e2);
    }

}
