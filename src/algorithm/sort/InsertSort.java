package algorithm.sort;

/**
 * 最原始的插入排序,时间O(n^2),空间O(1)
 * 稳定的排序算法 cmp(cur,cur-1) < 0
 * 逆序对的数量越多,插入排序的时间复杂度越高
 */
public class InsertSort<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin;
            while (cur > 0 && cmp(cur,cur-1) < 0) {
                swap(cur,cur-1);
                cur--;
            }
        }
    }

}
