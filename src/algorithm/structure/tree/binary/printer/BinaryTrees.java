package algorithm.structure.tree.binary.printer;

import algorithm.Main;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author MJ Lee
 */
public class BinaryTrees {
    private BinaryTrees() {
    }

    public static void print(BinaryTreeInfo tree) {
        print(tree, null);
    }

    public static void println(BinaryTreeInfo tree) {
        println(tree, null);
    }

    public static void print(BinaryTreeInfo tree, PrintStyle style) {
        if (tree == null || tree.root() == null) return;
        printer(tree, style).print();
    }

    public static void println(BinaryTreeInfo tree, PrintStyle style) {
        if (tree == null || tree.root() == null) return;
        printer(tree, style).println();
    }

    public static String printString(BinaryTreeInfo tree) {
        return printString(tree, null);
    }

    public static String printString(BinaryTreeInfo tree, PrintStyle style) {
        if (tree == null || tree.root() == null) return null;
        return printer(tree, style).printString();
    }

    private static Printer printer(BinaryTreeInfo tree, PrintStyle style) {
        if (style == PrintStyle.INORDER) return new InorderPrinter(tree);
        return new LevelOrderPrinter(tree);
    }

    public static void travel(BinaryTreeInfo tree){
        traversal(tree,null,null).travel();
    }

    public static void travel(BinaryTreeInfo tree,TravelStyle style){
        traversal(tree,style,null).travel();
    }

    public static void travel(BinaryTreeInfo tree,Visitor<?> visitor){
        traversal(tree,null,visitor).travel();
    }

    public static void travel(BinaryTreeInfo tree,TravelStyle style,Visitor<?> visitor){
        traversal(tree,style,visitor).travel();
    }

    public static void travel(BinaryTreeInfo tree,Visitor<?> visitor,TravelStyle style){
        traversal(tree,style,visitor).travel();
    }

    @SuppressWarnings("unchecked")
    private static Traversal traversal(BinaryTreeInfo tree, TravelStyle style, Visitor<?> visitor) {
        if (style == TravelStyle.IN_ORDER) return new InOrderTraversal(tree, (Visitor<Object>) visitor);
        if (style == TravelStyle.POST_ORDER) return new PostOrderTraversal(tree, (Visitor<Object>) visitor);
        if (style == TravelStyle.PRE_ORDER) return new PreOrderTraversal(tree, (Visitor<Object>) visitor);
        return new LevelOrderTraversal(tree, (Visitor<Object>) visitor);
    }

    //判断二叉树是否 完全二叉树
    public static boolean isCompleteTree(BinaryTreeInfo tree) {
        if (tree.root() == null) return false;

        Queue<Object> queue = new LinkedList<>();
        queue.offer(tree.root());
        //标志从此往后的节点都应是叶子节点
        boolean afterLeaf = false;

        while (!queue.isEmpty()){
            Object node = queue.poll();

            if (afterLeaf && !(tree.left(node)==null && tree.right(node) == null)) return false;

            if (tree.left(node) != null)
                queue.offer(tree.left(node));
            else if (tree.right(node) != null)
                //说明 node.left == null && node.right != null 不符合完全二叉树
                return false;

            if (tree.right(node) != null)
                queue.offer(tree.right(node));
            else
                //说明 ( node.left != null && node.right == null ) || ( node.left == null && node.right == null )
                //说明后面的节点都必须是叶子节点
                afterLeaf = true;
        }
        return true;
    }

    //判断二叉树是否 平衡二叉搜索树
    public static boolean isBalancedBinarySearchTree(BinaryTreeInfo tree) {
        return isBalancedBST(tree,tree.root()) >= 0;
    }

    /**
     * 自底向上递归写法 O(n)
     * @param root 二叉树根节点
     * @return 大于等于0是
     */
    private static int isBalancedBST(BinaryTreeInfo tree,Object root){
        if(tree == null || root == null) return 0;
        int leftHeight = isBalancedBST(tree,tree.left(root));
        int rightHeight = isBalancedBST(tree,tree.right(root));
        if (leftHeight==-1||rightHeight==-1||Math.abs(leftHeight-rightHeight)>1) return -1;
        return Math.max(leftHeight,rightHeight)+1;
    }

    //获取二叉树的高/深度
    public static int getTreeHeight(BinaryTreeInfo tree,ComputeStyle style){
        if (style == ComputeStyle.ITERATION) return _tree_height_iteration(tree);
        else return _tree_height_recursion(tree,tree.root());
    }

    private static int _tree_height_iteration(BinaryTreeInfo tree){
        //记录树的高度
        int height = 0;
        if (tree.root() == null) return height;
        //记录当前层有多少个节点
        int num = 1;
        Queue<Object> queue = new LinkedList<>();
        queue.offer(tree.root());
        while (!queue.isEmpty()){
            Object node = queue.poll();
            num--;
            if (tree.left(node) != null) queue.offer(tree.left(node));
            if (tree.right(node) != null) queue.offer(tree.right(node));
            if (num==0) {
                height++;
                num = queue.size();
            }
        }
        return height;
    }

    private static int _tree_height_recursion(BinaryTreeInfo tree,Object root){
        if (root == null) return 0;
        int left = _tree_height_recursion(tree,tree.left(root));
        int right = _tree_height_recursion(tree,tree.right(root));
        return Math.max(left,right) + 1;
    }

    /**
     * 获取二叉树的最大宽度
     * 将这棵二叉树从上到下&&从左到右对节点从 1 开始进行编号
     * 对任意第 i 个节点它的左子节点编号为 2i
     * 对任意第 i 个节点它的右子节点编号为 2i+1
     * 每层宽度 = (最左左子节点编号 - 最右右子节点编号) + 1
     */
    public static int getTreeWidth(BinaryTreeInfo tree) {
        int maxWidth = 1;
        //空直接返回
        if(tree.root() == null) return 0;
        //对节点进行编号
        java.util.LinkedList<Integer> ids = new java.util.LinkedList<>();
        java.util.Queue<Object> queue = new java.util.LinkedList<>();
        queue.offer(tree.root());
        ids.add(1);
        while(!queue.isEmpty()){
            int size = ids.size();
            for (int i = 0; i < size; i++) {
                Object node = queue.poll();
                int nid = ids.removeFirst();
                assert node != null;
                if (tree.left(node)!=null) {
                    queue.offer(tree.left(node));
                    ids.addLast(nid<<1);
                }
                if (tree.right(node)!=null) {
                    queue.offer(tree.right(node));
                    ids.addLast((nid<<1) + 1);
                }
            }
            //每当此层节点数量超过2个才有可比性
            if (ids.size()>1)
                maxWidth = Math.max(maxWidth,ids.getLast()-ids.getFirst()+1);
        }
        return maxWidth;
    }

    public enum ComputeStyle {
        RECURSION, ITERATION
    }

    public enum PrintStyle {
        LEVEL_ORDER, INORDER
    }
    public enum TravelStyle {
        LEVEL_ORDER, IN_ORDER, PRE_ORDER, POST_ORDER
    }
}