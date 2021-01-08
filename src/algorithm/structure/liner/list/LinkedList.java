package algorithm.structure.liner.list;

/**
 * 双向链表
 * Dynamic Array Implements By Linked
 * @see java.util.LinkedList
 * @param <E>
 */
public class LinkedList<E> extends AbstractList<E> implements List<E> {

    /**
     * @param <E> 定义一个Node节点类储存链表中每一个节点对象
     */
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;
        private Node(Node<E> prev, E value, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (prev!=null) sb.append(prev.value);
            else sb.append("null");
            sb.append("_").append(value).append("_");
            if (next!=null) sb.append(next.value);
            else sb.append("null");

            return sb.toString();
        }

        @Override
        protected void finalize() throws Throwable {
//            System.out.println("value : " + value + " died !");
            super.finalize();
        }
    }

    /**
     * 定义一个头(根)节点
     * first.prev = null
     */
    private Node<E> first;

    /**
     * 定义一个尾节点
     * last.next = null
     */
    private Node<E> last;

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
        if (index == size) {
            last = new Node<>(last,val,null);
            if (last.prev == null) {
                //链表刚创建时
                first = last;
            } else last.prev.next = last;
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> curr = new Node<>(prev, val, next);
            next.prev = curr;
            //当插在最前面时候
            if (prev == null)
                first = curr;
            else
                prev.next = curr;
        }
        size++;
    }

    @Override
    public void clear() {
        size = 0;
        //使之不再被gc-root对象引用,force gc to work
        //如果实现了迭代器,碰巧迭代器又被gc-root引用,则这些内存不会回收
        //因此java官方将节点之间关联也置空
        /*@see java.util.LinkedList#clear()*/
        first = null;
        last = null;
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
        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        //以下两对if和else肯定互斥
        if (prev==null)
            first = next;
        else prev.next = next;

        if (next==null)
            last = prev;
        else next.prev = prev;

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
//            b.append(node.value);
            b.append(node);
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
        //左半边使用头指针遍历
        Node<E> node;
        if (index > (size >> 1)) {
            node = first;
            for (int i = 0; i < index; i++)
                node = node.next;
        }
        //右半边使用尾指针遍历
        else {
            node = last;
            for (int i = size-1; i > index; i--)
                node = node.prev;
        }
        return node;
    }
}
