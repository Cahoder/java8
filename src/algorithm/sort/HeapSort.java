package algorithm.sort;

/**
 * 堆排序可以认为是对选择排序的一种优化,时间O(nLogN),空间O(1)
 * 不稳定的排序算法,交换堆顶元素和数组尾元素可能破坏稳定性
 */
public class HeapSort<E extends Comparable<E>> extends Sort<E>{
    private int heapSize;

    @Override
    protected void sort() {
        heapSize = array.length;

        //1.对数组进行原地建堆(heapify)
        for (int i = (heapSize >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }

        //2.重复以下步骤,直到堆的元素数量为一
            //2.1 交换堆顶元素和数组尾元素
            //2.2 堆的元素数量减一
            //2.3 对堆进行重新调整,恢复堆的性质
        while (heapSize > 1) {
            swap(0,--heapSize);
            siftDown(0);
        }
    }

    //调整保持堆的性质(自下向上)
    private void siftDown(int index) {
        E element = array[index];

        int half = heapSize >> 1;
        // index必须是非叶子节点
        while (index < half) {
            // 默认是左边跟父节点比
            int childIndex = (index << 1) + 1;
            E child = array[childIndex];

            int rightIndex = childIndex + 1;
            // 右子节点比左子节点大
            if (rightIndex < heapSize &&
                    cmp(array[rightIndex], child) > 0) {
                child = array[childIndex = rightIndex];
            }

            // 大于等于子节点
            if (cmp(element, child) >= 0) break;

            array[index] = child;
            index = childIndex;
        }
        array[index] = element;
    }

}
