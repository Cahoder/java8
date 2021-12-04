package algorithm.structure.union;

/**
 * 基于Quick Union思路实现并查集的优化思路2-1
 * 在基于rank优化的基础上,进行路径减半(Path Halving)
 * 即在find时使路径上每隔一个节点就指向其祖父节点(parent的parent)
 */
public class UnionFind_QU_R_PH extends UnionFind_QU_R {

    public UnionFind_QU_R_PH(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);
        while (v != parents[v]) {
            parents[v] = parents[parents[v]];
            v = parents[v];
        }
        return v;
    }
}
