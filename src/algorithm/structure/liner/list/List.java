package algorithm.structure.liner.list;

/**
 * Dynamic Array Implement
 * @param <E>
 */
public interface List<E> {
    /**
     * The Sign of Special Element Doesn't Exist In The Array
     */
    int ELEMENT_NOT_EXIST = -1;
    // 元素的数量
    int size();
    // 是否为空
    boolean isEmpty();
    // 是否包含某个元素
    boolean contains(E element);
    // 是否包含某个元素
    void add(int index, E element);
    // 清除所有元素
    void clear();
    // 查看元素的位置
    int indexOf(E element);
    // 删除元素对应index位置的元素
    void remove(E element);
    // 删除index位置对应的元素
    E remove(int index);
    // 设置index位置的元素
    E set(int index, E element);
    // 返回index位置对应的元素
    E get(int index);
    // 添加元素到最后面
    void add(E element);
}
