package algorithm.structure.liner.list;

/**
 * 双向循环链表
 * Dynamic Array Implements By Linked
 * @param <E>
 */
public class LinkedListCircle<E> extends AbstractList<E> implements List<E> {

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
            //System.out.println("value : " + value + " died !");
            super.finalize();
        }
    }

    /**
     * 定义一个头(根)节点
     * first.prev = last
     */
    private Node<E> first;

    /**
     * 定义一个尾节点
     * last.next = first
     */
    private Node<E> last;

    /**
     * 用于指向某个节点
     */
    private Node<E> current;

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
            last = new Node<>(last,val,first);
            if (last.prev == null) {
                //链表刚创建时
                last.next = last.prev = first = last;
            } else first.prev = last.prev.next = last;
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> curr = new Node<>(prev, val, next);
            next.prev = curr;
            prev.next = curr;
            //当插在最前面时候
            if (index == 0) first = curr;
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
    @Override
    public E remove(int index) {
        return remove(node(index));
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

    /**
     * @param node 根据指点节点删除
     * @return 被删除节点元素
     */
    private E remove(Node<E> node) {
        if (size == 1) {
            first = last = null;
        } else {
            Node<E> prev = node.prev;
            Node<E> next = node.next;

            prev.next = next;
            next.prev = prev;

            if (node == first) first = next;
            if (node == last) last = prev;
        }
        size--;
        return node.value;
    }

    /**
     * 让current重置指向头结点
     */
    public void reset() {
        current = first;
    }

    /**
     * 让 current 往后走一步
     * @return 返回走之后的元素
     */
    public E next() {
        if (current == null) return null;
        current = current.next;
        return current.value;
    }

    /**
     * 删除指向的元素后让 current 指向下一个节点
     * @return 被删除节点元素
     */
    public E remove(){
        if (current == null) return null;
        Node<E> next = current.next;
        E removed = remove(current);

        if (size == 0) current = null;
        else current = next;
        return removed;
    }
}
