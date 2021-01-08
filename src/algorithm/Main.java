package algorithm;

import algorithm.structure.liner.list.*;
import algorithm.structure.liner.queue.Deque;
import algorithm.structure.liner.queue.DequeCircle;
import algorithm.structure.liner.queue.Queue;
import algorithm.structure.liner.queue.QueueCircle;
import algorithm.structure.map.Map;
import algorithm.structure.map.TreeMap;
import algorithm.structure.set.ListSet;
import algorithm.structure.set.Set;
import algorithm.structure.set.TreeSet;
import algorithm.structure.tree.binary.AVL;
import algorithm.structure.tree.binary.BST;
import algorithm.structure.tree.binary.RBT;
import algorithm.structure.tree.binary.printer.BinaryTrees;
import algorithm.structure.tree.binary.printer.Visitor;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        MyListTest(new ArrayList<>());
//        MyListTest(new SingleLinkedList<>());
//        MyListTest(new SingleLinkedListCircle<>());
//        MyListTest(new LinkedList<>());
//        MyListTest(new LinkedListCircle<>());
        josephusProblem(8,3);
//        MyListTest(new ArrayListPro<>());
//        MyQueueTest();
//        MyTreeTest();
//        BST_AVL_RBT_EfficiencyTest();
//        InorderPostorderBuildTree(new int[]{9,3,15,20,7},new int[]{9,15,7,20,3},0,4,4);
//        PreorderInorderBuildTree(new int[]{3,9,20,15,7},new int[]{9,3,15,20,7},0,4,0);
//        PreorderPostorderBuildTree(new int[]{1,2,4,5,3,6,7},new int[]{4,5,2,6,7,3,1});
//        MySetTest();
//        ListSet_TreeSet_EfficiencyTest();
//        MyMapTest();
    }

    private static void MyMapTest() {
        Map<String,Integer> map = new TreeMap<>();
        System.out.println(map.put("b", 12));    //null
        map.put("a",12);
        map.put("c",12);
        System.out.println(map.put("c", 1));    //12
        map.put("d",2);
        map.put("f",3);
        map.put("g",4);
        map.put("e",5);
        map.put("h",2);
        map.put("i",3);
        map.put("j",4);
        map.put("k",5);
        System.out.println(map.get("z"));
        System.out.println(map.get("a"));
        map.traversal(new Map.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println("key_" + key + " value_" + value);
                return false;
            }
        });
    }

    /**
     * @see ListSet 添加/删除/搜索 的时间复杂度为 O(n)
     * @see TreeSet 添加/删除/搜索 的时间复杂度为 O(logn)
     */
    private static void ListSet_TreeSet_EfficiencyTest(){
        ArrayList<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100_000000; i++) datas.add((int) (Math.random() * 100));

        ListSet<Integer> listSet = new ListSet<>();
        TreeSet<Integer> treeSet = new TreeSet<>();

        /*
        【ListSet_Efficiency_Test】
        开始：15:25:44.332
        结束：15:26:13.381
        耗时：29.049秒
        */
        TestHelper.check("ListSet_Efficiency_Test", () -> {
            for (int i = 0; i < datas.size(); i++) listSet.add(datas.get(i));
            for (int i = 0; i < datas.size(); i++) listSet.contains(datas.get(i));
            for (int i = 0; i < datas.size(); i++) listSet.remove(datas.get(i));
        });
        /*
        【TreeSet_Efficiency_Test】
        开始：15:26:13.384
        结束：15:26:22.259
        耗时：8.875秒
        */
        TestHelper.check("TreeSet_Efficiency_Test", () -> {
            for (int i = 0; i < datas.size(); i++) treeSet.add(datas.get(i));
            for (int i = 0; i < datas.size(); i++) treeSet.contains(datas.get(i));
            for (int i = 0; i < datas.size(); i++) treeSet.remove(datas.get(i));
        });
    }

    //无重复元素集合测试用例
    private static void MySetTest() {
//        Set<Integer> set = new ListSet<>();
        Set<Integer> set = new TreeSet<>();
        set.add(10);
        set.add(11);
        set.add(12);
        set.add(10);
        set.remove(10);
        set.remove(10);
        set.remove(10);
        set.remove(10);
        set.traversal(new Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }

    private static void BST_AVL_RBT_EfficiencyTest() {
        ArrayList<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100_000000; i++) datas.add((int) (Math.random() * 100));

        BST<Integer> bst = new BST<>();
        AVL<Integer> avl = new AVL<>();
        RBT<Integer> rbt = new RBT<>();

        /*
        【BST_Efficiency_Test】
        开始：12:47:15.235
        结束：12:47:25.614
        耗时：9.897秒
        */
        TestHelper.check("BST_Efficiency_Test", () -> {
            for (int i = 0; i < datas.size(); i++) bst.add(datas.get(i));
            for (int i = 0; i < datas.size(); i++) bst.contains(datas.get(i));
            for (int i = 0; i < datas.size(); i++) bst.remove(datas.get(i));
        });
        /*
        【AVL_Efficiency_Test】
        开始：12:47:25.616
        结束：12:47:34.949
        耗时：9.342秒
        */
        TestHelper.check("AVL_Efficiency_Test", () -> {
            for (int i = 0; i < datas.size(); i++) avl.add(datas.get(i));
            for (int i = 0; i < datas.size(); i++) avl.contains(datas.get(i));
            for (int i = 0; i < datas.size(); i++) avl.remove(datas.get(i));
        });
        /*
        【RBT_Efficiency_Test】
        开始：12:47:34.950
        结束：12:47:44.857
        耗时：9.213秒
         */
        TestHelper.check("RBT_Efficiency_Test", () -> {
            for (int i = 0; i < datas.size(); i++) rbt.add(datas.get(i));
            for (int i = 0; i < datas.size(); i++) rbt.contains(datas.get(i));
            for (int i = 0; i < datas.size(); i++) rbt.remove(datas.get(i));
        });
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
    }

    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    //反转链表
    public ListNode ReverseList(ListNode head) {
        ListNode tmp, newHead = null;
        while (head!=null) {
            tmp = head.next;
            //插一个节点到newHead前
            head.next = newHead;
            //修改newHead新的头节点
            newHead = head;
            head = tmp;
        }
        return newHead;
    }

    /**
     * 给定一个二叉树和一个目标和
     * 判断该树中是否存在根节点到叶子节点的路径
     * 这条路径上所有节点值相加等于目标和
     * @param root 根节点
     * @param sum 路径总和
     * @return 是否存在一条根到叶子的路径总和符合要求
     */
    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        sum -= root.val;
        if (root.left == null && root.right == null && sum == 0) return true;

        boolean left = hasPathSum(root.left, sum);
        if (left) return true;

        return hasPathSum(root.right, sum);
    }

    //将升序链表转换成二叉搜索树
    public TreeNode sortedListToBST(ListNode head) {
        if(head == null) return null;
        if(head.next == null) return new TreeNode(head.val);
        // 快慢指针找中间结点
        ListNode fast = head, slow = head, prev = null;
        while(fast != null && fast.next != null){
            fast =  fast.next.next;
            prev = slow;
            slow = slow.next;
        }
        // 用中间结点构造根结点
        TreeNode root = new TreeNode(slow.val);
        // 构造左子树
        assert prev != null;
        prev.next = null; //掐断左子链表
        root.left = sortedListToBST(head);
        // 构造右子树
        root.right = sortedListToBST(slow.next);
        // 返回本结点所在子树
        return root;
    }

    /**
     * https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/
     * @param preorder 前序
     * @param postorder 后序
     * @return 前序+后序重构 二叉树
     * 因为前序+后序无法确定唯一的二叉树,因此返回只是其中一种情况
     */
    private static TreeNode PreorderPostorderBuildTree(int[] preorder, int[] postorder){
        //终止条件
        if (preorder==null || preorder.length==0) return null;

        TreeNode root = new TreeNode(preorder[0]);
        //只有一个元素直接返回
        if (preorder.length==1) return root;

        int postIdx = 0;
        while (postIdx < postorder.length && postorder[postIdx] != preorder[1]) postIdx++;

        root.left = PreorderPostorderBuildTree(Arrays.copyOfRange(preorder,1,postIdx+2)
                ,Arrays.copyOfRange(postorder,0,postIdx+1));
        root.right = PreorderPostorderBuildTree(Arrays.copyOfRange(preorder,postIdx+2,preorder.length)
                ,Arrays.copyOfRange(postorder,postIdx+1,postorder.length-1));
        return root;
    }

    /**
     * https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
     * @param inorder 中序
     * @param postorder 后序
     * @param inStart 中序的开始区间
     * @param inEnd 中序的结束区间
     * @param rootIdx 根节点在后序中的位置
     * @return 中序+后序重构 二叉树
     */
    private static TreeNode InorderPostorderBuildTree(int[] inorder, int[] postorder, int inStart, int inEnd, int rootIdx) {
        //终止条件
        if (inStart > inEnd) return null;

        TreeNode root = new TreeNode(postorder[rootIdx]);

        //获取后序根节点在中序中的索引位置
        int InRootIdx = inStart;
        while (InRootIdx <= inEnd && inorder[InRootIdx] != root.val) InRootIdx++;

        root.left = InorderPostorderBuildTree(inorder, postorder,  inStart, InRootIdx - 1,rootIdx - (inEnd - InRootIdx) - 1);
        root.right = InorderPostorderBuildTree(inorder,postorder, InRootIdx + 1, inEnd,rootIdx - 1);

        return root;
    }

    /**
     * https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
     * @param preorder 前序
     * @param inorder 中序
     * @param inStart 中序的开始区间
     * @param inEnd 中序的结束区间
     * @param rootIdx 根节点在前序中的位置
     * @return 前序+中序重构 二叉树
     */
    private static TreeNode PreorderInorderBuildTree(int[] preorder, int[] inorder, int inStart, int inEnd, int rootIdx) {
        //终止条件
        if (inStart > inEnd) return null;
        TreeNode root = new TreeNode(preorder[rootIdx]);

        //获取前序根节点在中序中的索引位置
        int InRootIdx = inStart;
        while (InRootIdx <= inEnd && inorder[InRootIdx] != root.val) InRootIdx++;

        root.left = PreorderInorderBuildTree(preorder, inorder,  inStart, InRootIdx - 1,rootIdx + 1);
        root.right = PreorderInorderBuildTree(preorder, inorder, InRootIdx + 1, inEnd,rootIdx + 1 + InRootIdx - inStart);

        return root;
    }

    private static void MyTreeTest() {
        int[] sss = new int[]{63, 72, 13, 1, 49, 68, 82, 60, 44, 3, 69, 12, 22, 35, 78, 19, 88, 66, 26};
//        BST<Integer> bst = new BST<>();
        BST<Integer> bst = new AVL<>();
//        BST<Integer> bst = new RBT<>();
        for (int i = 0; i < sss.length; i++) {
            bst.add(sss[i]);
//            bst.add((int) (Math.random() * 100));
        }
        BinaryTrees.println(bst);
        System.out.println("\n");
        /*for (int i = 0; i < sss.length; i++) {
            bst.remove(sss[i]);
            System.out.println("AfterRemoved : " + sss[i]);
            System.out.println("Is_Balanced_Binary_Search_Tree : " + BinaryTrees.isBalancedBinarySearchTree(bst));
            BinaryTrees.println(bst);
            System.out.println("\n");
        }*/
        System.out.println("Is_Contain_Element '2': " + bst.contains(2));
        int height = BinaryTrees.getTreeHeight(bst, BinaryTrees.ComputeStyle.ITERATION);
        System.out.println("Tree_Height : " + height);
        int width = BinaryTrees.getTreeWidth(bst);
        System.out.println("Tree_Width : " + width);
        boolean is = BinaryTrees.isCompleteTree(bst);
        System.out.println("Is_Complete_Tree : " + is);
        boolean bis = BinaryTrees.isBalancedBinarySearchTree(bst);
        System.out.println("Is_Balanced_Binary_Search_Tree : " + bis);
        System.out.print("Inorder Traversal : ");
        BinaryTrees.travel(bst, BinaryTrees.TravelStyle.IN_ORDER);

//        System.out.println("\n");
//        BinaryTrees.travel(bst, BinaryTrees.TravelStyle.PRE_ORDER);
//        System.out.println("\n");
//        BinaryTrees.travel(bst, BinaryTrees.TravelStyle.IN_ORDER);
//        System.out.println("\n");
//        BinaryTrees.travel(bst, BinaryTrees.TravelStyle.POST_ORDER);
//        System.out.println("\n");
//        BinaryTrees.travel(bst, BinaryTrees.TravelStyle.LEVEL_ORDER);
    }
    //队列测试用例
    private static void MyQueueTest(){

        //普通队列
        Queue<Integer> queue = new Queue<>();
        queue.enQueue(1);
        queue.enQueue(2);
        queue.enQueue(3);
        queue.enQueue(4);
        queue.enQueue(5);
        System.out.println(queue);
        while (!queue.empty())
            System.out.println(queue.deQueue());
        System.out.println("-------------普通队列----------------");
        //双端队列
        Deque<Integer> deque = new Deque<>();
        deque.enQueueFront(1);
        System.out.println(deque);
        deque.enQueueFront(2);
        System.out.println(deque);
        deque.enQueueRear(2);
        System.out.println(deque);
        deque.enQueueRear(1);
        System.out.println(deque);
        while (!deque.empty())
            System.out.println(deque.deQueueFront());
        System.out.println("--------------双端队列---------------");
        //普通循环队列
        QueueCircle<Integer> queueCircle = new QueueCircle<>();
        queueCircle.enQueue(1);
        queueCircle.enQueue(2);
        queueCircle.enQueue(3);
        queueCircle.enQueue(4);
        queueCircle.enQueue(5);
        System.out.println(queueCircle);
        while (!queueCircle.empty())
            System.out.println(queueCircle.deQueue());
        System.out.println("------------普通循环队列-----------------");
        //双端循环队列
        DequeCircle<Integer> dequeCircle = new DequeCircle<>();
        dequeCircle.enQueueFront(1);
        System.out.println(dequeCircle);
        dequeCircle.enQueueFront(2);
        System.out.println(dequeCircle);
        dequeCircle.enQueueRear(2);
        System.out.println(dequeCircle);
        dequeCircle.enQueueRear(1);
        System.out.println(dequeCircle);
        while (!dequeCircle.empty())
            System.out.println(dequeCircle.deQueueFront());
        System.out.println("------------双端循环队列-----------------");
    }
    //动态数组测试用例
    private static void MyListTest(List<Integer> list) {
        list.add(-1);
        System.out.println(list.remove(0));
        for (int i = 0; i < 50; i++) list.add(i);
        System.out.println(list.remove(49));
        System.out.println(list.contains(0));
        System.out.println(list);
        System.out.println(list.size());
        list.clear();
        //force invoke gc to work
        System.gc();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(0);
        list.add(null);
        list.add(list.size(),555);
        System.out.println(list);
        list.remove(list.size()-1);
        System.out.println(list);
        list.add(0,-1);
        System.out.println(list);
        System.out.println(list.indexOf(null));
        System.out.println(list.isEmpty());
        System.out.println(list);
    }
    //约瑟夫问题
    private static void josephusProblem(int totoal,int interval){
        LinkedListCircle<Integer> list = new LinkedListCircle<>();
        for (int i = 1; i <= totoal; i++) list.add(i);
        list.reset();
        StringBuilder order = new StringBuilder();
        order.append("[start->");
        while (!list.isEmpty()) {
            for (int i = 0; i < interval-1; i++) list.next();
            Integer removed = list.remove();
            order.append(removed).append("->");
        }
        System.out.println(order.append("end]").toString());
    }
}