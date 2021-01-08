package algorithm.sort;

import java.util.Arrays;

/**
 * 快速排序优化思路1: 随机选择轴点元素,降低最坏情况出现的概率
 */
public class QuickSort_1<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        sort(0,array.length);
    }

    /**
     * 对序列内某个子区间内的元素进行归并排序, [begin,end)
     * @param begin 区间的开始索引
     * @param end 区间的结束索引
     */
    private void sort(int begin, int end) {
        //如果区间内元素只有一个
        if (end - begin < 2) return;

        //寻找区间的轴点位置, O(n)
        int mid = pivotIndex(begin,end);

        //对左右子区间再进行一次快速排序, 最好|平均2*T(n/2) 最坏T(n-1)
        sort(begin,mid);
        sort(mid+1,end);
    }

    /**
     * 构造出 [begin,end) 区间内的轴点元素
     * @param begin 区间的开始索引
     * @param end 区间的结束索引
     * @return 轴点元素的索引位置
     */
    private int pivotIndex(int begin, int end) {
        //随机选择轴点元素
        swap(begin,begin + (int)(Math.random()*(end-begin)));

        //备份begin位置的元素作为轴点元素
        E pivot = array[begin];
        //因为[begin,end),需要保证end位置上有元素
        end--;

        while (begin < end) {
            //先从右往左找
            while (begin < end) {
                //如果右边元素 > 轴点元素   (这里不用>=是为效率考虑,能够将左右子序列分配更均匀,否则很容易出现最坏情况)
                if (cmp(array[end],pivot) > 0) {
                    end--;
                }
                //如果右边元素 <= 轴点元素
                else {
                    array[begin] = array[end];
                    begin++;
                    break;
                }
            }
            //再从左往右找
            while (begin < end) {
                //如果左边元素 < 轴点元素   (这里不用<=是为效率考虑,能够将左右子序列分配更均匀,否则很容易出现最坏情况)
                if (cmp(array[begin],pivot) < 0) {
                    begin++;
                }
                //如果左边元素 >= 轴点元素
                else {
                    array[end] = array[begin];
                    end--;
                    break;
                }
            }
        }

        //在轴点位置放入备份的轴点元素
        array[begin] = pivot;

        //此时begin==end==轴点位置
        return begin;
    }

}
