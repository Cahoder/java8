package algorithm.structure.tree.binary;

import algorithm.structure.tree.binary.printer.BinaryTreeInfo;

/**
 * 二叉树父类
 */
public abstract class BinaryTree<E>  implements BinaryTreeInfo {
    /**
     * Binary Search Tree Element's Size
     */
    protected int size;
    /**
     * The Root Node of this BST
     */
    protected Node<E> root;
    /**
     * @param <E> 定义一个Node类储存树节点元素
     */
    protected static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        public Node(E element, Node<E> parent){
            this.element = element;
            this.parent = parent;
        }
        public boolean isParentLeftChild(){
            return parent!=null && parent.left == this;
        }
        public boolean isParentRightChild(){
            return parent!=null && parent.right == this;
        }
        public Node<E> sibling() {
            if (isParentLeftChild()) return parent.right;
            if (isParentRightChild()) return parent.left;
            return null;
        }

        @Override
        public String toString() {
            boolean n = this.parent == null;
            if (n) return this.element + "_p(null)";
            return this.element + "_p("+this.parent.element+")";
        }
    }
    /**
     * @return The Element's size of this BST
     */
    public int size(){
        return size;
    }

    /**
     * @return Whether have more than one element in this BST
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Clear all the elements in this BST
     */
    public void clear(){
        root = null;
        size = 0;
    }

    /**
     * @return Create a new Node<E> Object
     */
    protected Node<E> createNode(E element, Node<E> parent){
        return new Node<>(element,parent);
    }

    /**
     * how to get the predecessor of the design node
     * The predecessor is the node which order before the design node in the inorder Traversal
     */
    protected Object predecessor(Node<E> node){
        if (node == null) return null;

        //如果左子树不为空,则按照中序遍历规则,前继节点肯定在左子树中最右节点
        Node<E> n = node.left;
        if (n!=null) {
            while (node.right!=null) n = n.right;
            return n;
        }
        //如果左子树为空,又分两种情况考虑
        while (node.parent!=null && node.parent.left == node) node = node.parent;

        //(1.parent==null说明无前继  2.node是parent的(左子它不是前继) || (右子它是前继))
        return node.parent;
    }

    /**
     * how to get the successor of the design node
     * The successor is the node which order behind the design node in the inorder Traversal
     */
    protected Node<E> successor(Node<E> node){
        if (node == null) return null;

        //如果右子树不为空,则按照中序遍历规则,后继节点肯定在右子树中最左节点
        Node<E> n = node.right;
        if (n!=null) {
            while (n.left!=null) n = n.left;
            return n;
        }
        //如果右子树为空,又分两种情况考虑
        while (node.parent!=null && node.parent.right == node) node = node.parent;

        //(1.parent==null说明无后继  2.node是parent的(左子它是后继) || (右子它不是后继))
        return node.parent;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object left(Object node) {
        if (node==null) return null;
        return ((Node<E>)node).left;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object right(Object node) {
        if (node==null) return null;
        return ((Node<E>)node).right;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parent(Object node) {
        if (node==null) return null;
        return ((Node<E>)node).parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object string(Object node) {
        //子类不要随便重写此方法
        if (node==null) return null;
        return ((Node<E>)node).element;
    }

    /**
     * @param element Element Argument Not Null Check
     */
    protected void elementNotNullCheck(E element) {
        if (element==null)
            throw new IllegalArgumentException("element must not be null !");
    }
}
