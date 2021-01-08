package algorithm.sort;

/**
 * 1945年由约翰·冯·诺伊曼提出
 * 最原始的归并排序,时间O(nLogN),空间O(N)=O(N/2左子序列 + logN递归寄存器)
 * 稳定的排序算法
 */
@SuppressWarnings("unchecked")
public class MergeSort<E extends Comparable<E>> extends Sort<E>{
    private E[] leftArray;

    @Override
    protected void sort() {
        leftArray = (E[]) new Comparable[array.length >> 1];
        sort(0,array.length);
    }

    /**
     * 对序列内某个子区间内的元素进行归并排序, [begin,end)
     * @param begin 区间的开始索引
     * @param end 区间的结束索引
     *
     * 推算归并排序的时间复杂度(递推式):
     *  设归并排序的时间复杂度为T(n)
     *     1. n>1时,时间T(n) = O(1) + O(1) + T(n/2) + T(n/2) + O(n) = 2*T(n/2) + O(n)
     *     2. n=1时,时间T(1) = O(1) + O(1) + O(1) + O(1) + O(1) = O(1)
     *     则 T(n)/n = [T(n/2)]/(n/2) + O(1)
     *  设 S(n) = T(n)/n
     *     则 S(1) = T(1)/1 = O(1)
     *       S(n) = S(n/2) + O(1) = S(n/4) + O(2) = ... = S(n/(2^k)) + O(k) = S(1) + O(logN) = O(logN)
     *  因此 T(n) = S(n)*n = O(nLogN)
     */
    private void sort(int begin, int end) {
        //当前区间内元素至少要有2个
        if (end - begin < 2) return;
        int mid = (begin + end) >> 1;
        //先分治
        sort(begin,mid);
        sort(mid,end);
        //后合并
        merge(begin,mid,end);
    }

    /**
     * 对序列内 [begin,mid) 和 [mid,end) 两个子区间内的元素进行排序合并
     * @param begin 区间的开始索引
     * @param mid 区间的中间索引
     * @param end 区间的结束索引
     */
    private void merge(int begin, int mid, int end) {
        //左子区间,基于leftArray
        int leftArray_begin = 0;
        int leftArray_end = mid-begin;
        //先将左子区间备份
        System.arraycopy(array,begin,leftArray,leftArray_begin,leftArray_end);

        //右子区间,基于array
        int right_begin = mid;
        int right_end = end;

        //标识已经调整的索引位置
        int merge_index = begin;

        //情况1: 左子序列用完,右子序列没用完,右边不用再排序(右边肯定是已有序状态)
        //情况2: 右子序列用完,左子序列没用完,将左边剩下的补充到右边即可
        //因此只要保证左边的排好序了,右边的就不用再排序了
        while (leftArray_begin < leftArray_end) {
            //如果右子序列没用完,并且左子序列中当前元素 > 右子序列中当前元素
            //cmp不能用 >= (会失去稳定性)
            if (right_begin < right_end && cmp(leftArray[leftArray_begin],array[right_begin]) > 0) {
                array[merge_index] = array[right_begin];
                right_begin++;
            }
            //如果右子序列用完,或者左子序列中当前元素 < 右子序列中当前元素
            else {
                array[merge_index] = leftArray[leftArray_begin];
                leftArray_begin++;
            }
            merge_index++;
        }
    }

}
