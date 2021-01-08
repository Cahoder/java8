package algorithm.sort;

/**
 * 最原始的冒泡排序,时间O(n^2),空间O(1)
 * 稳定的排序算法 cmp(begin,begin-1) < 0
 */
public class BubbleSort<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int end = array.length-1; end > 0 ; end--) {
            for (int begin = 1; begin <= end; begin++) {
                /*if (array[begin] < array[begin-1]) {
                    int tmp = array[begin];
                    array[begin] = array[begin-1];
                    array[begin-1] = tmp;
                }*/
                if (cmp(begin,begin-1) < 0) {
                    swap(begin,begin-1);
                }
            }
        }
    }

}
