package algorithm.sort;

/**
 * 冒泡排序优化思路2: 如果某一趟扫描发现数组尾部局部有序,则记录最后一次交换的位置,减少比较次数
 */
public class BubbleSort_2<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int end = array.length-1; end > 0 ; end--) {
            //sortedIndex的初始值为1,满足冒泡排序优化思路1
            int sortedIndex = 1;
            for (int begin = 1; begin <= end; begin++) {
                /*if (array[begin] < array[begin-1]) {
                    int tmp = array[begin];
                    array[begin] = array[begin-1];
                    array[begin-1] = tmp;
                    sortedIndex = begin;
                }*/
                if (cmp(begin,begin-1) < 0) {
                    swap(begin,begin-1);
                    sortedIndex = begin;
                }
            }
            end = sortedIndex;
        }
    }

}
