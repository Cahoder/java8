package algorithm.structure.heap;

import algorithm.structure.tree.binary.printer.BinaryTreeInfo;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Binary Heap 完全二叉堆
 * 此类默认为大根堆,物理存储采用数组
 * 符合完全二叉树的性质(索引计算父节点和左右子节点)
 */
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {

    /**
     * Dynamic Array Elements
     */
    private E[] elements;
    /**
     * Default Array Capacity
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * if you don't need a custom Comparator
     * E must have implemented the Comparable interface
     *
     * @see java.lang.Comparable
     */
    public BinaryHeap() {
        this(null, null);
    }

    /**
     * @param comparator Use Comparator rule if it's not null
     */
    public BinaryHeap(Comparator<E> comparator) {
        this(comparator, null);
    }

    /**
     * @param elements Init this heap with elements
     */
    public BinaryHeap(E[] elements) {
        this(null, elements);
    }

    /**
     * @param comparator Use Comparator rule if it's not null
     * @param elements Init this heap with elements
     */
    @SuppressWarnings("unchecked")
    public BinaryHeap(Comparator<E> comparator, E[] elements) {
        super(comparator);

        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            int capacity = Math.max(DEFAULT_CAPACITY, elements.length);
            this.elements = Arrays.copyOf(elements, capacity);
            this.size = elements.length;
            heapify();
        }
    }

    /**
     * Get the top element of this Heap
     *
     * @return top element
     */
    public E get() {
        emptyCheck();
        return elements[0];
    }

    /**
     * @param element Add a new element in this Heap
     */
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);
        elements[size++] = element;
        siftUp(size - 1);
    }

    /**
     * Remove the top element of this Heap
     * And add a new element in this Heap
     *
     * @param element Add new element
     * @return Removed top element
     */
    public E replace(E element) {
        elementNotNullCheck(element);
        E e = null;
        if (size == 0) {
            elements[0] = element;
            size = 1;
        } else {
            e = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return e;
    }

    /**
     * Remove the top element of this Heap
     *
     * @return Removed top element
     */
    public E remove() {
        emptyCheck();

        E element = elements[0];
        elements[0] = elements[size - 1];
        elements[size - 1] = null;
        size--;
        if (size > 1)
            siftDown(0);

        trimCapacity(size);
        return element;
    }

    /**
     * Clear all elements in this Heap
     */
    public void clear() {
        //invoke gc to work
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;

        trimCapacity(size);
    }

    /**
     * 让指定数组索引位置元素上滤
     * 时间复杂度O(log n)
     *
     * @param index 数组索引
     */
    private void siftUp(int index) {
        E element = elements[index];
        while (index > 0) {
            //完全二叉树数组实现获取父节点索引性质
            int parentIndex = (index - 1) >> 1;
            E parent = elements[parentIndex];
            //如果元素值小于等于父元素值
            if (compare(element, parent) <= 0) break;
            //如果元素值大于父元素值
            elements[index] = parent;
            index = parentIndex;
        }
        elements[index] = element;
    }

    /**
     * 让指定数组索引位置元素下滤
     * 时间复杂度O(log n)
     *
     * @param index 数组索引
     */
    private void siftDown(int index) {
        E element = elements[index];
        // 数组构成的完全二叉树性质
        // 首个叶子节点索引下标 = 非叶子节点数量
        int leafIndex = size >> 1;
        // 当且进当index位置是非叶子节点才要下滤
        while (index < leafIndex) {
            //index的子节点仅存在两种情形
            //1.只有左子节点
            int childIndex = (index << 1) + 1;
            E child = elements[childIndex];
            //2.存在左右子节点
            if (childIndex + 1 < size &&
                    compare(elements[childIndex + 1], child) > 0) {
                child = elements[++childIndex];
            }

            if (compare(element, child) >= 0) break;
            elements[index] = child;
            index = childIndex;
        }
        elements[index] = element;
    }

    /**
     * init this heap by unordered elements
     */
    private void heapify() {
        //自上而下的上滤原地建堆(首位元素无需上滤)（重复上滤性能较差）
        //for (int i = 1; i < size; i++) siftUp(i);

        //自下而上的下滤原地建堆(从尾个非叶子节点开始下滤)
        for (int i = (size >> 1)-1; i >= 0; i--) siftDown(i);
    }

    /**
     * @param capacity 确保当前堆容量足够下一次添加操作
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        //按照1.5倍因数扩容
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        elements = Arrays.copyOf(elements, newCapacity);
        //System.out.println("enlarger capacity : " + oldCapacity + " to " + newCapacity);
    }

    /**
     * @param capacity 当数组容量空闲空间过大需要进行缩容
     */
    private void trimCapacity(int capacity) {
        int oldCapacity = elements.length;
        //按照0.5倍因数缩容
        int newCapacity = oldCapacity >> 1;
        if (newCapacity <= capacity || oldCapacity <= DEFAULT_CAPACITY) return;

        elements = Arrays.copyOf(elements, newCapacity);
        //System.out.println("shrink capacity : " + oldCapacity + " to " + newCapacity);
    }

    @Override
    public Object root() {
        return isEmpty() ? null : 0;
    }

    @Override
    public Object left(Object node) {
        Integer index = (Integer) node;
        int leftIndex = (index << 1) + 1;
        return leftIndex >= size ? null : leftIndex;
    }

    @Override
    public Object right(Object node) {
        Integer index = (Integer) node;
        int rightIndex = (index << 1) + 2;
        return rightIndex >= size ? null : rightIndex;
    }

    @Override
    public Object parent(Object node) {
        Integer index = (Integer) node;
        int parentIndex = (index - 1) >> 1;
        return index == 0 ? null : parentIndex;
    }

    @Override
    public Object string(Object node) {
        Integer index = (Integer) node;
        return elements[index];
    }
}
