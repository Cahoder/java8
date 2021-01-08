package algorithm.sort;

/**
 * 冒泡排序优化思路1: 如果某一趟扫描发现数组已经有序,则停止
 */
public class BubbleSort_1<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int end = array.length-1; end > 0 ; end--) {
            boolean sorted = true;
            for (int begin = 1; begin <= end; begin++) {
                /*if (array[begin] < array[begin-1]) {
                    int tmp = array[begin];
                    array[begin] = array[begin-1];
                    array[begin-1] = tmp;
                    sorted = false;
                }*/
                if (cmp(begin,begin-1) < 0) {
                    swap(begin,begin-1);
                    sorted = false;
                }
            }
            if (sorted) break;
        }
    }

}
