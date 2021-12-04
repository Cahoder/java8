package algorithm.structure.union;

/**
 * 基于Quick Union思路实现并查集的优化思路2-1
 * 在基于rank优化的基础上,进行路径分裂(Path Splitting)
 * 即在find时使路径上的每个节点都指向其祖父节点(parent的parent)
 */
public class UnionFind_QU_R_PS extends UnionFind_QU_R {

    public UnionFind_QU_R_PS(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);
        while (v != parents[v]) {
            int parent = parents[v];
            parents[v] = parents[parent];
            v = parent;
        }
        return v;
    }
}
