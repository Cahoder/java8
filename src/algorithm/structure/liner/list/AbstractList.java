package algorithm.structure.liner.list;

/**
 * Abstract List Class
 * The Common Method of List's subClass
 * @param <E>
 */
public abstract class AbstractList<E> implements List<E> {
    /**
     * Dynamic Array Size
     */
    protected int size;

    /**
     * @return 元素的数量
     */
    public int size() {
        return size;
    }

    /**
     * @param element 元素
     * @return 是否包含某个元素
     */
    public boolean contains(E element) {
        return this.indexOf(element) != ELEMENT_NOT_EXIST;
    }

    /**
     * @return 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @param element 添加元素到最后面
     */
    public void add(E element) {
        add(size,element);
    }

    /**
     * @param element 删除元素对应的index位置的元素
     */
    public void remove(E element) {
        remove(indexOf(element));
    }

    /**
     * @param index index是否数组越界
     */
    protected void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * @param index 添加操作index是否数组越界
     */
    protected void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * @param index 用户给予的index值
     * @return 数组越界信息
     */
    protected String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
}
