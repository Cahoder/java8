package algorithm.structure.liner.list;

/**
 * 单向链表
 * Dynamic Array Implements By Linked
 * @param <E>
 */
public class SingleLinkedList<E> extends AbstractList<E> implements List<E> {

    /**
     * @param <E> 定义一个Node节点类储存链表中每一个节点对象
     */
    private static class Node<E> {
        E value;
        Node<E> next;
        private Node(E value,Node<E> next){
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 定义一个头(根)节点
     */
    private Node<E> first;

    /**
     * Get the value of the index-th node in the linked list. If the index is invalid, return -1.
     * */
    public E get(int index) {
        return node(index).value;
    }

    /** Add a node of value val before the index-th node in the linked list.
     * If index equals to the size of linked list, the node will be appended to the end of linked list.
     * If index is greater than the size, the node will not be inserted.
     * */
    public void add(int index, E val) {
        if (index == 0) {
            first = new Node<>(val,first);
        } else {
            Node<E> prev = node(index-1);
            prev.next = new Node<>(val,prev.next);
        }
        size++;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
    }

    @Override
    public int indexOf(E element) {
        Node<E> node = first;
        if (element==null) {
            for (int i = 0; i < size; i++) {
                if (node.value == null) return i;
                node = node.next;
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if (element.equals(node.value)) return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_EXIST;
    }

    /**
     * Delete the index-th node in the linked list, if the index is valid.
     * */
    public E remove(int index) {
        rangeCheck(index);

        Node<E> node = first;
        if (index == 0){
            first = first.next;
        } else {
            Node<E> prev = node(index - 1);
            node = prev.next;
            prev.next = node.next;
        }
        size--;
        return node.value;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = node(index);
        E oldElement = node.value;
        node.value = element;
        return oldElement;
    }

    @Override
    public String toString(){
        Node<E> node = first;
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; i < size; i++) {
            if(i > 0) b.append(", ");
            b.append(node.value);
            node = node.next;
        }
        return b.append(']').toString();
    }

    /**
     * @param index 链表索引index
     * @return 获取索引index所在的节点
     */
    private Node<E> node(int index){
        rangeCheck(index);
        Node<E> node = first;
        for (int i = 0; i < index; i++)
            node = node.next;
        return node;
    }
}
