package algorithm.structure.union;

/**
 * 基于Quick Union思路实现并查集的优化思路1
 * 使用固定方式union的过程中,可能会出现树不平衡的情况,甚至退化成链表
 * 因此可以基于size的优化:元素少的集合 嫁接到 元素多的集合
 * 但这种情况还是有可能会出现树不平衡的情况,例如元素少树高与元素多树矮
 */
public class UnionFind_QU_S extends UnionFind_QU {
    /**
     * 存储每个元素所在的集合的元素数量
     */
    private final int[] sizes;

    public UnionFind_QU_S(int capacity) {
        super(capacity);

        sizes = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            sizes[i] = 1;
        }
    }

    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        if (sizes[p1] < sizes[p2]) {
            parents[p1] = p2;
            sizes[p2] += sizes[p1];
        } else {
            parents[p2] = p1;
            sizes[p1] += sizes[p2];
        }
    }
}
