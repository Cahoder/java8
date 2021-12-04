package algorithm.structure.union;

/**
 * 原始基于Quick Find思路实现并查集
 * 并查集形成的树的高度绝对不会超过2
 * 查找(find)的时间复杂度:O(1)
 * 合并(union)的时间复杂度:O(N)
 */
public class UnionFind_QF extends UnionFind {

    public UnionFind_QF(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);
        return parents[v];
    }

    @Override
    /* 实现原理: 将跟v1同集合的所有元素全部转移到v2所在集合的根元素上 */
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        for (int i = 0; i < parents.length; i++) {
            if (parents[i] == p1) {
                parents[i] = p2;
            }
        }
    }
}
