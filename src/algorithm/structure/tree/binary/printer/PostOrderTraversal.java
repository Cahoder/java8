package algorithm.structure.tree.binary.printer;

import java.util.Stack;

public class PostOrderTraversal extends Traversal{

    public PostOrderTraversal(BinaryTreeInfo tree, Visitor<Object> visitor) {
        super(tree, visitor);
    }

    //用枚举表示标志位
    private enum Tags{LEFT, RIGHT}

    //标志当前节点左右子节点访问状态
    private static class NodeStatus{
        Object pointer;
        Tags tags;
        public NodeStatus(Object pointer, Tags tags) {
            this.pointer = pointer;
            this.tags = tags;
        }
    }

    @Override
    public void travel() {
        //travel(tree.root());

        Object pointer = tree.root();
        if (pointer==null) return;

        NodeStatus node;
        Stack<NodeStatus> stack = new Stack<>();

        while (!stack.isEmpty()||pointer!=null){
            while (pointer!=null){
                stack.push(new NodeStatus(pointer,Tags.LEFT));  //表示进入其左子节点
                pointer = tree.left(pointer);
            }
            node = stack.pop();
            pointer = node.pointer;
            if (node.tags == Tags.LEFT) {  //说明其左子节点访问过,该访问其右子节点了
                node.tags = Tags.RIGHT;
                stack.push(node);
                pointer = tree.right(pointer);
            }
            else {  //说明其左右子节点都访问过,该访问其本身了
                visitor.stop = visitor.visit(tree.string(pointer));
                pointer = null;     //驱使继续弹栈
                if (visitor.stop) return;
            }
        }
    }

    //递归写法
    public void travel(Object root) {
        if (root==null || visitor.stop) return;
        travel(tree.left(root));
        travel(tree.right(root));
        if (visitor.stop) return;
        visitor.stop = visitor.visit(tree.string(root));
    }
}