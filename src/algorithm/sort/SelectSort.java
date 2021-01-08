package algorithm.sort;

/**
 * 最原始的选择排序,交换次数远远小于冒泡排序,
 * 因此平均性能优于冒泡,时间O(n^2),空间O(1)
 * 稳定的排序算法,if (array[maxIndex] <= array[begin])
 */
public class SelectSort<E extends Comparable<E>> extends Sort<E>{
    @Override
    protected void sort() {
        for (int end = array.length-1; end > 0 ; end--) {
            int maxIndex = 0;
            /*
                优化思路: 使用堆结构来进行maxIndex的选择
                @see algorithm.sort.HeapSort#sort()
             */
            for (int begin = 1; begin <= end; begin++) {
                /*if (array[maxIndex] <= array[begin]) {
                    maxIndex = begin;
                }*/
                if (cmp(maxIndex,begin) <= 0) {
                    maxIndex = begin;
                }
            }
            /*int tmp = array[maxIndex];
            array[maxIndex] = array[end];
            array[end] = tmp;*/
            swap(maxIndex,end);
        }
    }
}
