package algorithm.structure.tree.binary.printer;

/**
 * 以何种方式查看元素以及是否停止遍历
 */
public abstract class Visitor<E> {
    /**
     * 是否停止遍历
     */
    boolean stop;

    /**
     * This method will stop if return <code>true</code>
     * or continue if return <code>false</code>
     * @param element element which will be visiting
     * @return whether to visit next one
     */
    public abstract boolean visit(E element);
}
