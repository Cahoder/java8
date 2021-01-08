package algorithm.structure.tree.binary;

import java.util.Comparator;

/**
 * AVL树是在二叉搜索树的基础上产生的
 * 一种能够自平衡的二叉搜索树
 * 其任意节点的左右子树高度差小于1
 * @param <E>
 */
public class AVL<E> extends BBST<E>{
    public AVL(Comparator<E> comparator) {
        super(comparator);
    }

    public AVL() {
        this(null);
    }

    private static class AVLNode<E> extends Node<E> {
        int height = 1;
        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }
        public int balanceFactor(){
            int leftHeight = this.left==null ? 0 : ((AVLNode<E>)this.left).height;
            int rightHeight = this.right==null ? 0 : ((AVLNode<E>)this.right).height;
            return leftHeight-rightHeight;
        }
        public void updateHeight(){
            int leftHeight = this.left==null ? 0 : ((AVLNode<E>)this.left).height;
            int rightHeight = this.right==null ? 0 : ((AVLNode<E>)this.right).height;
            this.height = Math.max(leftHeight,rightHeight) + 1;
        }
        public Node<E> tallerChild(){
            int leftHeight = this.left==null ? 0 : ((AVLNode<E>)this.left).height;
            int rightHeight = this.right==null ? 0 : ((AVLNode<E>)this.right).height;
            //左右子树高度不同,返回较高的子节点
            if (leftHeight > rightHeight) return left;
            if (leftHeight < rightHeight) return right;
            //左右子树同等高度,返回与父节点同方向的子节点
            if (isParentLeftChild()) return left;
            if (isParentRightChild()) return right;
            //左右子树都为空
            return null;
        }

        @Override
        public String toString() {
            boolean n = this.parent == null;
            if (n) return this.element + "_p(null)_h("+this.height+")";
            return this.element + "_p("+this.parent.element+")_h("+this.height+")";
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element,parent);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        //向上找父节点
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                //更新高度
                updateHeight(node);
            }
            else {
                //恢复高度最低的不平衡父节点即可,一次就好
                //它必然也是node的祖父节点
//                reBalance(node);
                reBalance_common(node);
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //向上找父节点
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                //更新高度
                updateHeight(node);
            }
            else {
                //删除节点仅导致父节点或祖先节点(其中1个节点)失衡
                //但旋转调整后可能导致更高层的祖先节点失衡
//                reBalance(node);
                reBalance_common(node);
            }
        }
    }

    /**
     * Recover the node's balance include:
     * <ol>
     * <code>LL</code> operation
     * <code>RR</code> operation
     * <code>LR</code> operation
     * <code>RL</code> operation
     * </ol>
     * @param grand_parent The Node with
     * the smallest tree height Which is unbalanced
     */
    private void reBalance(Node<E> grand_parent) {
        Node<E> parent = ((AVLNode<E>) grand_parent).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        //L
        if (parent.isParentLeftChild()){
            //LL
            if (node.isParentLeftChild()) zig(grand_parent);
            //LR
            else if (node.isParentRightChild()){
                zag(parent);
                zig(grand_parent);
            }
        }
        //R
        else if (parent.isParentRightChild()) {
            //RL
            if (node.isParentLeftChild()) {
                zig(parent);
                zag(grand_parent);
            }
            //RR
            else if (node.isParentRightChild()) zag(grand_parent);
        }
    }

    /**
     * Recover the node's balance include:
     * <ol>
     * <code>LL</code> operation
     * <code>RR</code> operation
     * <code>LR</code> operation
     * <code>RL</code> operation
     * </ol>
     * @param grand_parent The Node with
     * the smallest tree height Which is unbalanced
     */
    private void reBalance_common(Node<E> grand_parent) {
        Node<E> parent = ((AVLNode<E>) grand_parent).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        //L
        if (parent.isParentLeftChild()){
            //LL
            if (node.isParentLeftChild())
                zag_zig_common(grand_parent,node.left,node,node.right,parent,parent.right,grand_parent,grand_parent.right);
            //LR
            else if (node.isParentRightChild())
                zag_zig_common(grand_parent,parent.left,parent,node.left,node,node.right,grand_parent,grand_parent.right);
        }
        //R
        else if (parent.isParentRightChild()) {
            //RL
            if (node.isParentLeftChild())
                zag_zig_common(grand_parent,grand_parent.left,grand_parent,node.left,node,node.right,parent,parent.right);
                //RR
            else if (node.isParentRightChild())
                zag_zig_common(grand_parent,grand_parent.left,grand_parent,parent.left,parent,node.left,node,node.right);
        }
    }

    /**
     * After update some property
     * AVL Tree must to update tree height
     * @param node Unbalanced Node's old grandson
     * @param parent Unbalanced Node's old son
     * @param grand_parent Unbalanced Node
     */
    @Override
    protected void zagged_zigged(Node<E> node, Node<E> parent, Node<E> grand_parent) {
        super.zagged_zigged(node, parent, grand_parent);
        //先更新grand_parent的高度
        updateHeight(grand_parent);
        //后更新parent的高度
        updateHeight(parent);
    }

    /**
     * 无论是左旋还是右旋它们旋转之后树的结果都是一样
     * 但是AVL树还需要更新树高度
     * @param r Unbalanced Tree's original root node
     * @param d The new root node after rotate
     */
    @Override
    protected void zag_zig_common(Node<E> r, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        super.zag_zig_common(r, a, b, c, d, e, f, g);
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    /**
     * @param node Update the node's tree height
     * Which depending on the bigger height of
     * its left sub tree height and its right sub tree height
     */
    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }

    /**
     * @param node it must to be a AVLNode<E> Object
     * @return Whether the distinction between
     * the left subTree's height and the right subTree's height
     * of the param node no more than one
     */
    private boolean isBalanced(Node<E> node){
        return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
    }
}
