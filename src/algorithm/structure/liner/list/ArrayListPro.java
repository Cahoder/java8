package algorithm.structure.liner.list;

/**
 * Dynamic Array Implements By Array
 * 对动态数组的进一步优化,使之成为循环数组
 * 使用first标识头元素
 * @param <E>
 */
public class ArrayListPro<E> extends AbstractList<E> implements List<E> {
    /**
     * Dynamic Array Elements
     */
    private Object[] elements;
    /**
     * Default Array Capacity
     */
    private static final int DEFAULT_CAPACITY = 10;
    /**
     * The Head Element's index of the Array
     */
    private int first;
    /**
     * 根据默认数组大小创建
     */
    public ArrayListPro(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * @param initCapacity 根据指定大小创建数组
     */
    public ArrayListPro(int initCapacity){
        initCapacity = Math.max(initCapacity, DEFAULT_CAPACITY);
        elements = new Object[initCapacity];
        size = 0;
        first = elements.length;
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        //invoke gc to work
        for (int i = 0; i < size; i++)
            setElements(i,null);
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
                if (elements(i) == null) return i;
        }
        else {
            for (int i = 0; i < size; i++)
                if (element.equals(elements(i))) return i;
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

        int mid = size >> 1;
        if (index < mid) {
            //左半边元素后移且first++
            for (int i = index; i > 0 ; i--) setElements(i,elements(i-1));
            //invoke gc to work,否则elements[0]有可能长时间存在内存中
            setElements(0,null);
            first++;
            size--;
        }
        else {
            //右半边元素前移
            int numMoved = size - index - 1;
            if (numMoved > 0)
                System.arraycopy(elements, modCapacity(index+first+1), elements, modCapacity(index+first), numMoved);
            //invoke gc to work,否则elements[--size]有可能长时间存在内存中
            setElements(--size,null);
        }

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
        setElements(index,element);
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

        if (index!=size) {
            //需要元素迁移
            for (int i = -1; i < index; )
                setElements(i,elements(++i));
            first--;
        }
        setElements(index,element);
        size++;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; i < size; i++) {
            if(i > 0) b.append(", ");
            b.append(elements(i));
        }
//        for (int i = 0; i < elements.length; i++) {
//            if(i > 0) b.append(", ");
//            b.append(elements[i]);
//        }
        return b.append(']').toString();
    }

    /**
     * @param index 用户给予的index值
     * @return 对象类型转换
     */
    @SuppressWarnings("unchecked")
    private E elements(int index) {
        return (E) elements[modCapacity(first+index)];
    }

    /**
     * @param index index 用户给予的index值
     * @param element 对给予的index赋值
     */
    private void setElements(int index, E element){
        //System.out.println(index + " mod: " + (first+index) + " = " + modCapacity(first+index));
        elements[modCapacity(first+index)] = element;
    }

    /**
     * @param capacity 确保当前数组容量足够下一次添加操作
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        //按照1.5倍因数扩容
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++)
            newElements[i] = elements(i);
        elements = newElements;
        first = newCapacity;
        //System.out.println("enlarger capacity : " + oldCapacity + " to " + newCapacity);
    }

    /**
     * @param capacity 当数组容量空闲空间过大需要进行缩容
     */
    private void trimCapacity(int capacity) {
        int oldCapacity = elements.length;
        //按照0.5倍因数缩容
        int newCapacity = oldCapacity >> 1;
        if (oldCapacity <= DEFAULT_CAPACITY || newCapacity <= capacity) return;

        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++)
            newElements[i] = elements(i);
        elements = newElements;
        first = newCapacity;
        //System.out.println("shrink capacity : " + oldCapacity + " to " + newCapacity);
    }

    /**
     * @param index 需要进行取模运算的数值
     * @return index 对数组容量进行取模之后新的 index 位置
     */
    private int modCapacity(int index){
        //return index % elements.length;
        //当n>=0 && m>=0 && n<2m, n%m 等价于 n – (m > n ? 0 : m)
        return index - (elements.length > index ? 0 : elements.length);
    }
}
