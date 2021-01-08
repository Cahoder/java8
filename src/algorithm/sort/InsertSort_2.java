package algorithm.sort;

/**
 * 插入排序优化思路2: 利用二分搜索优化,只能减少比较次数,
 * 但是挪动次数跟排序优化思路1一样
 */
public class InsertSort_2<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            E e = array[begin];
            //在已经排好序的那一部分中找到第一个大于当前元素的元素索引位置
            int insertIndex = indexOfFirstBiggerThan(begin);
            //从第一个大于当前元素的元素索引位置往后进行挪动操作
            System.arraycopy(array, insertIndex, array, insertIndex + 1, begin - insertIndex);
            array[insertIndex] = e;
        }
    }

    /**
     * 找到指定元素在已排好序那一部分序列中的待插入位置
     * @param eIdx 指定元素的索引位置
     * @return 找到第一个大于指定元素的元素索引位置
     */
    private int indexOfFirstBiggerThan(int eIdx) {
        int begin = 0;
        int end = eIdx;
        while (begin < end) {
            int mid = (begin+end) >> 1;
            //如果: 指定元素的值 < 中间元素的值,去左半部分
            if (cmp(array[eIdx],array[mid]) < 0) {
                end = mid;
            }
            //如果: 指定元素的值 >= 中间元素的值,去右半部分
            else {
                begin = mid+1;
            }
        }
        return begin;
    }

}
