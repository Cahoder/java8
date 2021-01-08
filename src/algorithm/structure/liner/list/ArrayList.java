package algorithm.structure.liner.list;

import java.util.Arrays;

/**
 * Dynamic Array Implements By Array
 * @param <E>
 */
public class ArrayList<E> extends AbstractList<E> implements List<E> {
    /**
     * Dynamic Array Elements
     */
    private Object[] elements;
    /**
     * Default Array Capacity
     */
    private static final int DEFAULT_CAPACITY = 10;
    /**
     * 根据默认数组大小创建
     */
    public ArrayList(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * @param initCapacity 根据指定大小创建数组
     */
    public ArrayList(int initCapacity){
        initCapacity = Math.max(initCapacity, DEFAULT_CAPACITY);
        elements = new Object[initCapacity];
        size = 0;
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        //invoke gc to work
        for (int i = 0; i < size; i++)
            elements[i] = null;
        size = 0;

        trimCapacity(size);
    }

    /**
     * @param element 查看元素
     * @return 查看元素的位置
     */
    public int indexOf(E element) {
        if (element==null) {
            for (int i = 0; i < size; i++)
                if (elements[i] == null) return i;
        }
        else {
            for (int i = 0; i < size; i++)
                if (element.equals(elements[i])) return i;
        }
        return ELEMENT_NOT_EXIST;
    }

    /**
     * @param index 删除index
     * @return 删除index位置对应的元素
     */
    public E remove(int index) {
        rangeCheck(index);
        E oldElement = elements(index);

        //从index开始元素前移
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elements, index+1, elements, index, numMoved);

        //invoke gc to work,否则elements[--size]有可能一直存在内存中删不掉
        elements[--size] = null;

        trimCapacity(size);

        return oldElement;
    }

    /**
     * @param index 设置index
     * @param element 元素
     * @return 设置index位置的元素
     */
    public E set(int index, E element) {
        rangeCheck(index);

        E oldElement = elements(index);
        elements[index] = element;
        return oldElement;
    }

    /**
     * @param index 返回index
     * @return 返回index位置对应的元素
     */
    public E get(int index) {
        rangeCheck(index);
        return elements(index);
    }



    /**
     * @param index 往index
     * @param element 往index位置添加元素
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacity(size+1);
        //从index开始元素后移
        System.arraycopy(elements, index, elements, index + 1, size - index);

        elements[index] = element;
        size++;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; i < size; i++) {
            if(i > 0) b.append(", ");
            b.append(elements[i]);
        }
        return b.append(']').toString();
    }

    /**
     * @param index 用户给予的index值
     * @return 对象类型转换
     */
    @SuppressWarnings("unchecked")
    private E elements(int index) {
        return (E) elements[index];
    }

    /**
     * @param capacity 确保当前数组容量足够下一次添加操作
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
}
