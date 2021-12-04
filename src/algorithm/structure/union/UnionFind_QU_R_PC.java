package algorithm.structure.union;

/**
 * 基于Quick Union思路实现并查集的优化思路2-1
 * 在基于rank优化的基础上,进行路径压缩(Path Compression)
 * 即在find时使路径上的所有节点都指向根节点,从而降低树的高度
 * 但是由于递归调用所在路径上所有节点,所以实现成本稍高
 */
public class UnionFind_QU_R_PC extends UnionFind_QU_R {

    public UnionFind_QU_R_PC(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        if (v != parents[v]) {
            parents[v] = find(parents[v]);
        }
        return parents[v];
    }
}
