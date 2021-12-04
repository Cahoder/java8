package algorithm.structure.union;

/**
 * 基于Quick Union思路实现并查集的优化思路2
 * 使用固定方式union的过程中,可能会出现树不平衡的情况,甚至退化成链表
 * 因此可以基于rank的优化:矮的树 嫁接到 高的树
 * 虽然这种做法树的高度会相对平衡一些,但是随着union次数的增多,树的高度依然会越来越高
 */
public class UnionFind_QU_R extends UnionFind_QU {
    /**
     * 存储每个元素所在的集合的树高度
     */
    private final int[] ranks;

    public UnionFind_QU_R(int capacity) {
        super(capacity);

        ranks = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            ranks[i] = 1;
        }
    }

    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        if (ranks[p1] < ranks[p2]) {
            parents[p1] = p2;
        }
        else if (ranks[p1] > ranks[p2]) {
            parents[p2] = p1;
        }
        //只有当两颗树高度相同,随意嫁接然后更新树高度
        else {
            parents[p1] = p2;
            ranks[p2]++;
        }
    }
}
