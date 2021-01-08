package algorithm.structure.tree.binary;

import java.util.Comparator;

/**
 * Balanced Binary Search Tree
 * 能够自动调整平衡的二叉搜索树
 * @param <E>
 */
public class BBST<E> extends BST<E>{
    public BBST(Comparator<E> comparator) {
        super(comparator);
    }

    public BBST() {
        this(null);
    }

    /**
     * After Rotate We need to update some property
     * @param node Unbalanced Node's old grandson
     * @param parent Unbalanced Node's old son
     * @param grand_parent Unbalanced Node
     */
    protected void zagged_zigged(Node<E> node,Node<E> parent,Node<E> grand_parent) {
        /*以下更新顺序不可随意改变*/
        //更新node的parent属性
        if (node!=null)
            node.parent = grand_parent;
        //更新parent的parent属性
        parent.parent = grand_parent.parent;
        if (grand_parent.isParentLeftChild()) grand_parent.parent.left = parent;
        else if (grand_parent.isParentRightChild()) grand_parent.parent.right = parent;
        else root = parent; //当grant_parent是根
        //更新grand_parent的parent属性
        grand_parent.parent = parent;
    }

    /**
     * RR
     * @param grand_parent Rotate Left
     */
    protected void zag(Node<E> grand_parent){
        Node<E> parent = grand_parent.right;
        Node<E> node = parent.left;

        grand_parent.right = node;
        parent.left = grand_parent;
        //更新节点属性和高度
        zagged_zigged(node,parent,grand_parent);
    }

    /**
     * LL
     * @param grand_parent Rotate Right
     */
    protected void zig(Node<E> grand_parent){
        Node<E> parent = grand_parent.left;
        Node<E> node = parent.right;

        grand_parent.left = node;
        parent.right = grand_parent;
        //更新节点属性和高度
        zagged_zigged(node,parent,grand_parent);
    }

    /**
     * 无论是左旋还是右旋它们旋转之后树的结果都是一样
     * @param r Unbalanced Tree's original root node
     * @param d The new root node after rotate
     */
    protected void zag_zig_common(
            Node<E> r,
            Node<E> a,Node<E> b,Node<E> c,
            Node<E> d,
            Node<E> e,Node<E> f,Node<E> g
    ){
        //让d成为这颗子树新的根节点
        d.parent = r.parent;
        if (r.isParentLeftChild()) r.parent.left = d;
        else if (r.isParentRightChild()) r.parent.right = d;
        else root = d;

        //处理a-b-c
        b.left = a;
        if (a!=null) a.parent = b;
        /*下面两句如果只是用于AVL和红黑树可以不写*/
        b.right = c;
        if (c!=null) c.parent = b;

        //处理e-f-g
        f.left = e;
        if (e!=null) e.parent = f;
        /*下面两句如果只是用于AVL和红黑树可以不写*/
        f.right = g;
        if (g!=null) g.parent = f;

        //处理b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
    }
}
