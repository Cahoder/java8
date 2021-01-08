package algorithm.sort;

/**
 * 插入排序优化思路1: 将交换转化为挪动
 */
public class InsertSort_1<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin;
            E v = array[cur];
            while (cur > 0 && cmp(v,array[cur-1]) < 0) {
                array[cur] = array[cur-1];
                cur--;
            }
            array[cur] = v;
        }
    }

}
