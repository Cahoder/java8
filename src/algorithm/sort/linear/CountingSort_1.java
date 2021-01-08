package algorithm.sort.linear;

import algorithm.sort.Sort;

/**
 * 计数排序优化思路1: 时间O(n+k) = O(n) + O(n) + O(k) + O(n) + O(n) ,空间O(n+k) = O(无序序列的长度) + O(数据的范围max-min+1)
 *      1. 通过找到最小值min和最大值max,优化计数需要开辟的空间(max-min+1),array中元素k相对索引是counts[k-min]
 *      2. 优化计数数组counts中记录的次数累加上其前面的所有次数
 *      3. 通过公式counts[k – min] - 1,得到的就是无序序列中元素在有序序列中的位置信息
 *      4. 从右往左遍历无序序列调用公式,就可以保证排序的稳定性,并且能够对负整数进行排序
 * 稳定的排序算法
 */
public class CountingSort_1 extends Sort<Integer> {
    @Override
    protected void sort() {
        //找出序列的最小值和最大值
        Integer min = array[0];
        Integer max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (cmp(array[i],max) > 0) max = array[i];
            if (cmp(array[i],min) < 0) min = array[i];
        }

        //开辟出相应的空间进行排序
        int[] counts = new int[max-min+1];
        for (Integer num : array) {
            counts[num-min]++;
        }
        //对counts中记录的次数累加上其前面的所有次数
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i-1];
        }

        //从右往左遍历无序序列调用公式
        Integer[] new_array = new Integer[array.length];
        for (int i = array.length-1; i > -1; i--) {
            new_array[--counts[array[i] - min]] = array[i];
        }

        System.arraycopy(new_array,0,array,0,array.length);
    }
}
