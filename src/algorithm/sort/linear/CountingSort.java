package algorithm.sort.linear;

import algorithm.sort.Sort;

/**
 * 线性排序: 用空间换时间,不是基于比较的排序
 * 最原始的计数排序,只能针对正整数,时间O(n),空间O(n)
 * 适合对一定范围内的数据进行排序
 * 不稳定的排序算法
 */
public class CountingSort extends Sort<Integer> {
    @Override
    protected void sort() {
        //找出序列的最大值
        Integer max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (cmp(array[i],max) > 0) max = array[i];
        }

        //开辟出相应的空间进行排序
        int[] counts = new int[max+1];
        for (Integer num : array) {
            counts[num]++;
        }

        int arrayIdx = 0;
        for (int i = 0; i < counts.length; i++) {
            while (counts[i]-- > 0) {
                array[arrayIdx++] = i;
            }
        }
    }
}
