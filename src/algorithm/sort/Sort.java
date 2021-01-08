package algorithm.sort;

/**
 * 十大排序算法
 * 默认进行升序排序
 */
public abstract class Sort<E extends Comparable<E>> {

    protected E[] array;
    private int cmpCount;
    private int swapCount;

    /**
     * 对外提供排序函数
     * @param array 待排序整形数组
     */
    public void sort(E[] array){
        this.array = array;
        if (array == null || array.length < 2) return;
        cmpCount = 0;
        swapCount = 0;
        this.sort();
    }

    /**
     * 需要指定的排序算法去实现
     */
    protected abstract void sort();

    /**
     * 比较数组中两个索引所在元素的大小关系
     * @param i1 array中元素索引
     * @param i2 array中元素索引
     * @return 正数, 表示 array[i1] > array[i2]
     *         0, 表示 array[i1] == array[i2]
     *         负数, 表示 array[i1] < array[i2]
     */
    protected int cmp(int i1, int i2){
        cmpCount++;
        return array[i1].compareTo(array[i2]);
    }

    /**
     * 比较数组中两个元素的大小关系
     * @param e1 array中元素
     * @param e2 array中元素
     * @return 正数, 表示 e1 > e2
     *         0, 表示 e1 == e2
     *         负数, 表示 e1 < e2
     */
    protected int cmp(E e1, E e2){
        cmpCount++;
        return e1.compareTo(e2);
    }

    /**
     * 交换数组中的元素
     * @param i1 array中元素索引
     * @param i2 array中元素索引
     */
    protected void swap(int i1, int i2) {
        swapCount++;
        E tmp = array[i1];
        array[i1] = array[i2];
        array[i2] = tmp;
    }

    /**
     * @return 获取整个排序过程中发生比较的次数
     */
    public int getCmpCount() {
        return cmpCount;
    }

    /**
     * @return 获取整个排序过程中发生交换的次数
     */
    public int getSwapCount() {
        return swapCount;
    }
}