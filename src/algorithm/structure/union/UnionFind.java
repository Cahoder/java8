package algorithm.structure.union;

/**
 * 并查集,也叫不相交集合(Disjoint Set),非常适合解决"连接"相关问题
 * 查询 连接的均摊时间复杂度为O(a(n)), a(n) < 5
 */
public abstract class UnionFind {
    /**
     * 使用数组存储数据
     */
    protected final int[] parents;

    /**
     * 初始化数据数组
     * @param capacity 数组容量
     */
    public UnionFind(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Capacity must be more than one!");
        parents = new  int[capacity];

        for (int i = 0; i < capacity; i++) {
            parents[i] = i;
        }
    }

    /**
     * @param v 数组中指定元素
     * @return 查找数组中指定元素所在的集合(根节点)
     */
    public abstract int find(int v);

    /**
     * 将数组中两个指定元素所在的集合合并为一个集合
     * @param v1 数组中指定元素v1
     * @param v2 数组中指定元素v2
     */
    public abstract void union(int v1,int v2);

    /**
     * @param v1 数组中指定元素v1
     * @param v2 数组中指定元素v2
     * @return 判断数组中两个指定元素所在的集合是否相同
     */
    public boolean isSame(int v1,int v2) {
        return find(v1) == find(v2);
    }

    /**
     * @param v 指定元素在数组中的范围检测
     */
    protected void rangeCheck(int v) {
        if (v < 0 || v >= parents.length) throw new IllegalArgumentException("v out of bounds!");
    }

}
