package algorithm.sort.linear;

import algorithm.sort.Sort;

import java.util.Arrays;

/**
 *  线性排序: 用空间换时间,不是基于比较的排序
 *  最原始的基数排序,只能针对正整数,时间O(d*(n+k)),空间O(n+k)  d是最大数值的位数,k是进制范围
 *  稳定的排序算法
 */
public class RadixSort extends Sort<Integer> {

    @Override
    protected void sort() {
        //找出序列的最大值
        Integer max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (cmp(array[i],max) > 0) max = array[i];
        }

        int[] counts = new int[10]; //10进制只有0~9
        Integer[] new_array = new Integer[array.length];
        for (int divider = 1; divider <= max; divider *= 10) {
            //先重置一下计数数组
            Arrays.fill(counts, 0);
            //从低位到高位进行计数排序
            for (Integer num : array) {
                counts[num / divider % 10]++;
            }
            //对counts中记录的次数累加上其前面的所有次数
            for (int i = 1; i < counts.length; i++) {
                counts[i] += counts[i-1];
            }
            //从右往左遍历无序序列调用公式
            for (int i = array.length-1; i > -1; i--) {
                new_array[--counts[array[i] / divider % 10]] = array[i];
            }

            System.arraycopy(new_array,0,array,0,array.length);
        }
    }

}
