package algorithm.structure.set;

import algorithm.structure.liner.list.LinkedList;
import algorithm.structure.liner.list.List;
import algorithm.structure.tree.binary.printer.Visitor;

/**
 * 使用线性表实现
 * 无重复元素的集合
 * @param <E>
 * 整体性能为O(n)级别
 * 允许元素E为null
 * 元素E无限制
 */
public class ListSet<E> implements Set<E> {
    /**
     * Liner DataStructure
     */
    private final List<E> elements = new LinkedList<>();

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
        //时间复杂度O(n)
        return elements.contains(element);
    }

    @Override
    public void add(E element) {
        //时间复杂度O(n)
        int index = elements.indexOf(element);
        if (index != List.ELEMENT_NOT_EXIST)
            elements.set(index,element);
        else elements.add(element);
    }

    @Override
    public void remove(E element) {
        //时间复杂度O(n)
        int index = elements.indexOf(element);
        if (index != List.ELEMENT_NOT_EXIST)
            elements.remove(index);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        if (visitor == null) return;
        int size = size();
        for (int i = 0; i < size; i++) {
            boolean stop = visitor.visit(elements.get(i));
            if (stop) return;
        }
    }

}
