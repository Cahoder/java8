package algorithm.sort.linear;

import algorithm.sort.Sort;

/**
 *  基数排序的另一种思路实现,时间O(d*n),空间O(k*n + k)  d是最大数值的位数,k是进制范围
 *  稳定的排序算法
 */
public class RadixSort_1 extends Sort<Integer> {

    @Override
    protected void sort() {
        //找出序列的最大值
        Integer max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (cmp(array[i],max) > 0) max = array[i];
        }

        //使用一个二维数组作为基数桶数组
        int[][] buckets = new int[10][array.length];
        //记录每个桶中的元素数量
        int[] bucketSizes = new int[buckets.length];
        for (int divider = 1; divider <= max; divider *= 10) {
            //将基数相同的元素加入同一列桶中,O(n)
            for (Integer num : array) {
                int no = num / divider % 10;
                buckets[no][bucketSizes[no]++] = num;
            }
            //回收桶中的元素,O(n)
            int index = 0;
            for (int i = 0; i < buckets.length; i++) {
                for (int j = 0; j < bucketSizes[i]; j++) {
                    array[index++] = buckets[i][j];
                }
                bucketSizes[i] = 0;
            }
        }
    }

}
