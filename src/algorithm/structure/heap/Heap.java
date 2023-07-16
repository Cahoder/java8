package algorithm.structure.heap;

/**
 * 堆 - 堆中的元素必须具备可比较性（跟二叉搜索树一样）
 * 任意节点的值总是( ≥ 或 ≤ )子节点的值
 * 如果任意节点的值总是 ≥ 子节点的值，称为：最大堆、大根堆、大顶堆
 * 如果任意节点的值总是 ≤ 子节点的值，称为：最小堆、小根堆、小顶堆
 * 获取最(大|小)值 -> O(1)
 * 添加元素 -> O(log n)
 * 删除元素 -> O(log n)
 * @param <E>
 * @see java.util.PriorityQueue
 */
public interface Heap<E> {

    // 元素的数量
    int size();

    // 是否为空
    boolean isEmpty();

    // 清空
    void clear();

    // 添加元素
    void add(E element);

    // 获得堆顶元素
    E get();

    // 删除堆顶元素
    E remove();

    // 删除堆顶元素的同时插入一个新元素
    E replace(E element);

}
