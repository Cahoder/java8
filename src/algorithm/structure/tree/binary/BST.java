package algorithm.structure.tree.binary;

import java.util.Comparator;

/**
 * Binary Search Tree
 * @param <E>
 */
//二叉搜索树的元素必须具备可比较性
//但是直接强制要求E必须实现可比较接口不合理
//因此可以兼容两种方式 : 第一种实现可比较接口 第二种传个比较器给我
//public class BST<E extends Comparable> {
public class BST<E> extends  BinaryTree<E> {
    /**
     * A Comparator If you have new custom compare rule
     */
    private final Comparator<E> comparator;

    /**
     * @param comparator Use Comparator rule if it's not null
     */
    public BST(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * if you don't need a custom Comparator
     * E must have implemented the Comparable interface
     * @see java.lang.Comparable
     */
    public BST() {
        this(null);
    }

    /**
     * @param element Add a new element in this BST
     */
    public void add(E element){
        elementNotNullCheck(element);
        if (root == null) {
            root = createNode(element,null);
            size++;
            afterAdd(root);
            return;
        }

        Node<E> node = root;
        Node<E> parentNode = null;
        int cmp = 0;
        while (node!=null) {
            cmp = compare(element, node.element);
            parentNode = node;
            //说明当前元素比较大应该往右边走
            if (cmp > 0) node = node.right;
            //说明当前元素比较小应该往左边走
            else if (cmp < 0) node = node.left;
            //元素一样大新覆盖旧并返回
            else {
                node.element = element;
                return;
            }
        }

        Node<E> nNode = createNode(element, parentNode);
        if (cmp > 0) parentNode.right = nNode;
        else parentNode.left = nNode;

        size++;
        afterAdd(nNode);
    }

    /**
     * How to do after add a new node into BST
     * Which use into <code>Balance Binary Search Tree<code/>
     * @param node new node
     */
    protected void afterAdd(Node<E> node){}

    /**
     * How to do after remove a node into BST
     * Which use into <code>Balance Binary Search Tree<code/>
     * @param node deleted node or replacement node which is the node's predecessor or successor
     */
    protected void afterRemove(Node<E> node){}

    /**
     * @param element Remove the design element of this BST
     * @return Removed element
     */
    public E remove(E element){
        elementNotNullCheck(element);
        remove(node(element));
        return element;
    }

    /**
     * @param element Whether contain the design element in this BST
     * @return Contained element
     */
    public boolean contains(E element) {
        elementNotNullCheck(element);
        return node(element) != null;
    }

    /**
     * @param element Get Node By Element
     * @return return <code>null</code> if element doesn't exist
     */
    private Node<E> node(E element) {
        Node<E> node = root;
        while (node!=null) {
            int cmp = compare(element,node.element);
            if (cmp==0) return node;
            if (cmp>0) node = node.right;
            else node = node.left;
        }
        return null;
    }

    /**
     * @param node Remove the design node
     */
    private void remove(Node<E> node) {
        if (node==null) return;

        //节点度为2特殊处理,只有当度为2时前驱/后继都存在,只需找到它的前驱/后继代替它即可
        if (node.left!=null && node.right!=null) {
            //找到后继节点
            Node<E> s = successor(node);
            //用后继节点元素覆盖
            node.element = s.element;
            //前驱或后继节点度为0|1,可用以下代码
            node = s;
        }

        //统一删除节点(度为0|1)
        Node<E> replacement = (node.left!=null)?node.left:node.right;

        //当度为1的时候
        if (replacement!=null) {
            replacement.parent = node.parent;
            if (replacement.parent==null) {
                //说明当前节点是根
                root = replacement;
            } else if (node.parent.left == node) {
                //当删除节点是父节点的左子
                node.parent.left = replacement;
            } else {
                //当删除节点是父节点的右子
                node.parent.right = replacement;
            }
            //这里传替代节点不会影响AVL树,红黑树会用到
            afterRemove(replacement);
        }
        //当度为0的时候
        else {
            if (node.parent == null) {
                //说明当前节点是根
                root = null;
            } else if (node.parent.left == node) {
                //当删除节点是父节点的左子
                node.parent.left = null;
            } else {
                //当删除节点是父节点的右子
                node.parent.right = null;
            }
            afterRemove(node);
        }
        size--;
    }

    /**
     * @param e1 the first object to be compared.
     * @param e2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the second.
     */
    @SuppressWarnings("unchecked")
    private int compare(E e1,E e2){
        if (comparator!=null) return comparator.compare(e1,e2);
        //Use the E default comparable rule
        //We assume that the E have implements the java.lang.comparable interface
        //if E not it will throw a runtime exception
        return ((Comparable<E>)e1).compareTo(e2);
    }
}
