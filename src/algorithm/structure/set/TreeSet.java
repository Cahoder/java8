package algorithm.structure.set;

import algorithm.structure.map.Map;
import algorithm.structure.map.TreeMap;
import algorithm.structure.tree.binary.RBT;
import algorithm.structure.tree.binary.printer.BinaryTrees;
import algorithm.structure.tree.binary.printer.Visitor;

import java.util.Comparator;

/**
 * 使用二叉树结构的红黑树实现
 * 无重复元素的集合
 * @param <E>
 * 整体性能为O(logn)级别
 * 不允许元素E为null
 * 缺陷: 元素E必须具备可比较性
 */
/*
public class TreeSet<E> implements Set<E> {

    //使用红黑树存放节点
    private final RBT<E> elements;

    public TreeSet() {
        this(null);
    }

    public TreeSet(Comparator<E> comparator) {
        elements = new RBT<>(comparator);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean contains(E element) {
        //时间复杂度O(logn)
        return elements.contains(element);
    }

    @Override
    public void add(E element) {
        //时间复杂度O(logn)
        //红黑树是二叉搜索树,添加自带去重功能
        elements.add(element);
    }

    @Override
    public void remove(E element) {
        //时间复杂度O(logn)
        elements.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        if (visitor == null) return;
        BinaryTrees.travel(elements,visitor, BinaryTrees.TravelStyle.IN_ORDER);
    }
}
*/

/**
 * 使用TreeMap的Key实现TreeSet
 * @param <E>
 */
public class TreeSet<E> implements Set<E> {
    //只需用到TreeMap的键
    Map<E,Object> map = new TreeMap<>();
    //使用一个常量对象作为值
    private static final Object value = new Object();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public void add(E element) {
        map.put(element,value);
    }

    @Override
    public void remove(E element) {
        map.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        map.traversal(new Map.Visitor<E, Object>() {
            @Override
            public boolean visit(E key, Object value) {
                return visitor.visit(key);
            }
        });
    }
}