package algorithm.structure.union;

/**
 * 原始基于Quick Union思路实现并查集
 * 并查集形成的树的高度可能会超过2
 * 查找(find)的时间复杂度:O(logN)
 * 合并(union)的时间复杂度:O(logN)
 */
public class UnionFind_QU extends UnionFind {

    public UnionFind_QU(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);
        while (v != parents[v]) {
            v = parents[v];
        }
        return v;
    }

    @Override
    /* 实现原理: 将v1所在集合的根元素转移到v2所在集合的根元素上 */
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        parents[p1] = p2;
    }
}
