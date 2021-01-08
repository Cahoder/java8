package algorithm.sort.linear;

import algorithm.sort.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 *  线性排序: 用空间换时间,不是基于比较的排序
 *  最原始的桶排序,稳定的排序算法
 *  执行流程:
 *      1.创建一定数量的桶(比如用数组或链表作为桶)
 *      2.按照一定的规则(不同类型的数据,规则不同),将序列中的元素均匀分配到对应的桶
 *      3.分别对每个桶进行单独排序
 *      4.将所有非空桶的元素合并成有序序列
 *  空间复杂度:O(n + m), m是桶的数量
 *  时间复杂度:O(n + k), k为(n*logN - n*logM)
 */
@SuppressWarnings("unchecked")
public class BucketSort extends Sort<Double> {

    @Override
    protected void sort() {
        //创建一定数量的桶集合
        List<Double>[] buckets = new List[array.length];
        for (Double num : array) {
            int bucketIndex = (int) (num * array.length);
            List<Double> bucket = buckets[bucketIndex];
            if (bucket == null) {
                bucket = new LinkedList<>();
                buckets[bucketIndex] = bucket;
            }
            bucket.add(num);
        }
        //对每个桶进行排序,并回收桶内排好的元素
        int index = 0;
        for (List<Double> bucket : buckets) {
            if (bucket == null) continue;
            bucket.sort(null);
            for (Double num : bucket) {
                array[index++] = num;
            }
        }
    }

}
