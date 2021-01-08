package algorithm.structure.tree.binary;

import java.util.Comparator;

/**
 * Red Black Tree 红黑树
 * 一种能够自平衡的二叉搜索树
 * @param <E>
 */
public class RBT<E> extends BBST<E> {
    /**
     * The Flag Design the Red Black Tree Node's Color
     */
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBT() {
        this(null);
    }

    public RBT(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * Dye the Red Black Node's Color
     * @param node The Red Black Node need to dye
     * @param color dying color
     * @return The Red Black Node have been dyed
     */
    private Node<E> dye(Node<E> node,boolean color){
        if (node!=null)
            ((RBTNode<E>) node).color = color;
        return node;
    }

    /**
     * Dye the Red Black Node to <code>RED</code> color
     * @param node The Red Black Node need to dye
     * @return The Red Black Node have been dyed
     */
    private Node<E> dyeRed(Node<E> node){
        return dye(node,RED);
    }

    /**
     * Dye the Red Black Node to <code>BLACK</code> color
     * @param node The Red Black Node need to dye
     * @return The Red Black Node have been dyed
     */
    private Node<E> dyeBlack(Node<E> node){
        return dye(node,BLACK);
    }

    /**
     * @param node Red Black Node
     * @return Get the color of the Red Black Node
     */
    private boolean colorOf(Node<E> node){
        return node == null ? BLACK : ((RBTNode<E>) node).color;
    }

    /**
     * @param node Red Black Node
     * @return Whether the color of the Red Black Node is RED
     */
    private boolean isRed(Node<E> node){
        return colorOf(node) == RED;
    }

    /**
     * @param node Red Black Node
     * @return Whether the color of the Red Black Node is BLACK
     */
    private boolean isBlack(Node<E> node){
        return colorOf(node) == BLACK;
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBTNode<>(element,parent);
    }

    /**
     * Red Black Node
     * @param <E>
     */
    private static class RBTNode<E> extends Node<E> {
        boolean color = RED;    //让红黑树的性质尽快满足
        public RBTNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            boolean n = this.parent == null;
            String color = this.color == RED ? "red" : "black";
            if (n) return this.element + "_p(null)_c("+ color +")";
            return this.element + "_p("+this.parent.element+")_c("+ color +")";
        }
    }

    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;

        //如果当前是根节点,将根节点染成黑色
        if (parent == null) {
            dyeBlack(node);
            return;
        }

        //如果父节点是黑色,无需做额外处理的4种情况
        if (isBlack(parent)) return;

        //如果父节点是红色,需做额外处理的8种情况(Double-RED)
        Node<E> uncle = node.parent.sibling();
        Node<E> grand_parent = parent.parent;
        //如果叔父节点是红色,在B树的角度它上溢
        if (isRed(uncle)) {
            dyeBlack(parent);
            dyeBlack(uncle);
            //将祖父节点向上染成红色并且向上合并---可以看成是添加新的节点
            afterAdd(dyeRed(grand_parent));
            return;
        }
        //如果叔父节点是黑色,需要分情况染色并旋转操作
        //L
        if (parent.isParentLeftChild()){
            //LL
            if (node.isParentLeftChild()) {
                dyeBlack(parent);
                dyeRed(grand_parent);
                zig(grand_parent);
            }
            //LR
            else if (node.isParentRightChild()) {
                dyeBlack(node);
                dyeRed(grand_parent);
                zag(parent);
                zig(grand_parent);
            }
        }
        //R
        else if (parent.isParentRightChild()) {
            //RL
            if (node.isParentLeftChild()) {
                dyeBlack(node);
                dyeRed(grand_parent);
                zig(parent);
                zag(grand_parent);
            }
            //RR
            else if (node.isParentRightChild()) {
                dyeBlack(parent);
                dyeRed(grand_parent);
                zag(grand_parent);
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //如果删除的节点是RED,无需额外操作,并可合并到BLACK节点有一个RED子节点情况
        //if (isRed(node)) return;
        //如果删除的节点是BLACK,需分情况额外操作
        //1.如果BLACK节点有2个RED子节点,无需额外操作
        //2.如果BLACK节点有1个RED子节点,需要将代替其的RED子节点染成BLACK
        if (isRed(node)){
            dyeBlack(node);
            return;
        }

        //3.如果BLACK节点是叶子节点
        Node<E> parent = node.parent;
        //3.1如果BLACK节点是根节点,直接删除
        if (parent==null) return;

        //叶子节点被执行完remove方法后,父节点肯定有一边是null了,则它的兄弟节点就是另一边
        //但是afterRemove方法不一定是给remove调用,可能是递归调用,不能完全依靠null来判断
        boolean isParentLeftChild = (parent.left == null) || node.isParentLeftChild();
        Node<E> sibling = isParentLeftChild ? parent.right : parent.left;

        //如果原本是父节点的左子,则兄弟节点在右
        if (isParentLeftChild) {
            //3.2如果BLACK节点的兄弟节点为RED
            if (isRed(sibling)) {
                dyeBlack(sibling);
                dyeRed(parent);
                zag(parent);
                sibling = parent.right;  //此时换新兄弟
            }
            //3.3如果BLACK节点的兄弟节点为BLACK
            //3.2.1如果兄弟节点没有RED子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                boolean oldParentColor = colorOf(parent);
                dyeBlack(parent);
                dyeRed(sibling);
                //如果父节点原本是黑色,删除后必定会导致父节点继续下溢,需将父节点当做被删除节点递归调用
                if (oldParentColor == BLACK) afterRemove(parent);
            }
            //3.2.2如果兄弟节点至少有1个RED子节点
            else {
                if (isBlack(sibling.right)) {
                    zig(sibling);
                    sibling = parent.right;
                }

                //先对新的parent继承旧parent的颜色,新parent的子节点都染成黑色
                dye(sibling,colorOf(parent));
                dyeBlack(sibling.right);
                dyeBlack(parent);

                zag(parent);
            }
        }
        //如果原本是父节点的右子,则兄弟节点在左
        else {
            //3.2如果BLACK节点的兄弟节点为RED
            if (isRed(sibling)) {
                dyeBlack(sibling);
                dyeRed(parent);
                zig(parent);
                sibling = parent.left;  //此时换新兄弟
            }
            //3.3如果BLACK节点的兄弟节点为BLACK
            //3.2.1如果兄弟节点没有RED子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                boolean oldParentColor = colorOf(parent);
                dyeBlack(parent);
                dyeRed(sibling);
                //如果父节点原本是黑色,删除后必定会导致父节点继续下溢,需将父节点当做被删除节点递归调用
                if (oldParentColor == BLACK) afterRemove(parent);
            }
            //3.2.2如果兄弟节点至少有1个RED子节点
            else {
                if (isBlack(sibling.left)) {
                    zag(sibling);
                    sibling = parent.left;
                }

                //先对新的parent继承旧parent的颜色,新parent的子节点都染成黑色
                dye(sibling,colorOf(parent));
                dyeBlack(sibling.left);
                dyeBlack(parent);

                zig(parent);
            }
        }
    }
}
