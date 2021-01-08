package algorithm.structure.tree.binary.printer;

import java.util.LinkedList;
import java.util.Queue;

public class LevelOrderTraversal extends Traversal {

    public LevelOrderTraversal(BinaryTreeInfo tree, Visitor<Object> visitor) {
        super(tree, visitor);
    }

    @Override
    public void travel() {
        Queue<Object> queue = new LinkedList<>();
        queue.offer(tree.root());
        while (!queue.isEmpty()){
            Object node = queue.poll();
            visitor.stop = visitor.visit(tree.string(node));
            if (visitor.stop) return;
            if (tree.left(node) != null) queue.offer(tree.left(node));
            if (tree.right(node) != null) queue.offer(tree.right(node));
        }
    }
}
