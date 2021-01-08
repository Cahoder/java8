package algorithm.structure.tree.binary.printer;

import java.util.Stack;

public class InOrderTraversal extends Traversal{

    public InOrderTraversal(BinaryTreeInfo tree, Visitor<Object> visitor) {
        super(tree, visitor);
    }

    @Override
    public void travel() {
        //travel(tree.root());

        Object pointer = tree.root();
        Stack<Object> stack = new Stack<>();
        while (!stack.empty() || pointer!=null) {
            if (pointer!=null) {
                stack.push(pointer);
                pointer = tree.left(pointer);
            } else {
                pointer = stack.pop();
                visitor.stop = visitor.visit(tree.string(pointer));
                if (visitor.stop) return;
                pointer = tree.right(pointer);
            }
        }
    }

    //递归写法
    public void travel(Object root) {
        if (root==null || visitor.stop) return;
        travel(tree.left(root));
        if (visitor.stop) return;
        visitor.stop = visitor.visit(tree.string(root));
        travel(tree.right(root));
    }
}
