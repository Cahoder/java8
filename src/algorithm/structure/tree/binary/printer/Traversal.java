package algorithm.structure.tree.binary.printer;

import java.util.Date;

/**
 * 遍历二叉树
 */
public abstract class Traversal {
    /**
     * 二叉树的基本信息
     */
    protected BinaryTreeInfo tree;
    /**
     * 二叉树节点元素浏览器
     */
    protected Visitor<Object> visitor;

    public Traversal(BinaryTreeInfo tree, Visitor<Object> visitor) {
        this.tree = tree;
        this.visitor = (visitor != null) ? visitor : new Visitor<Object>() {
            @Override
            public boolean visit(Object element) {
                System.out.print("->" + element);
                return false;
            }
        };
    }

    /**
     * 进行二叉树遍历
     */
    public abstract void travel();
}
