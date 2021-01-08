package algorithm.structure.set;

import algorithm.structure.tree.binary.printer.Visitor;

/**
 * 非重复元素集合接口
 * @see java.util.Set
 * @param <E> element
 */
public interface Set<E> {
    /**
     * @return return the size of the set
     */
    int size();

    /**
     * @return whether the set is empty
     */
    boolean isEmpty();

    /**
     * clear the elements in the set
     */
    void clear();

    /**
     * @param element the designated element
     * @return whether contain the designated
     * element in this set
     */
    boolean contains(E element);

    /**
     * @param element add a new element into this set
     */
    void add(E element);

    /**
     * @param element remove a designated element from this set
     */
    void remove(E element);

    /**
     * @see Visitor
     * @param visitor How to traversal this by special rule
     */
    void traversal(Visitor<E> visitor);
}
