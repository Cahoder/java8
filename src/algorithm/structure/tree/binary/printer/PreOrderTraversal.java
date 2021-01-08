package algorithm.structure.tree.binary.printer;


import java.util.Stack;

public class PreOrderTraversal extends Traversal {

    public PreOrderTraversal(BinaryTreeInfo tree, Visitor<Object> visitor) {
        super(tree, visitor);
    }

    @Override
    public void travel() {
        //travel(tree.root());

        Stack<Object> stack = new Stack<>();
        Object pointer = tree.root();
        stack.push(null);   //放置监视哨
        while (pointer!=null) {
            visitor.stop = visitor.visit(tree.string(pointer));
            if (visitor.stop) return;
            if (tree.right(pointer)!=null) stack.push(tree.right(pointer));
            if (tree.left(pointer)!=null) pointer = tree.left(pointer);
            else pointer = stack.pop();
        }
    }

    //递归写法
    public void travel(Object root) {
        if (root==null || visitor.stop) return;
        visitor.stop = visitor.visit(tree.string(root));
        travel(tree.left(root));
        travel(tree.right(root));
    }
}
