package algorithm.sort;

import algorithm.ExamplesHelper;
import algorithm.TestHelper;
import algorithm.sort.linear.*;

import java.util.Arrays;

/**
 * 排序测试
 */
public class Test {
    public static void BubbleSortTest() {
        Integer[] examples = ExamplesHelper.random(10000, 0, 10000);
        Integer[] ascExamples = ExamplesHelper.ascOrder(0,10000);

        Integer[] tailAscOrderExamples = ExamplesHelper.tailAscOrder(1,10000,200);
        Integer[] tailAscOrderExamples1 = ExamplesHelper.copy(tailAscOrderExamples);

        System.out.println("----------------如果数组本身有序,那么可以进行优化----------------");
        TestHelper.check("原始冒泡排序测试", () -> {
            BubbleSort<Integer> bubbleSort = new BubbleSort<>();
            bubbleSort.sort(examples);
            System.out.println("比较次数: " + bubbleSort.getCmpCount());
            System.out.println("交换次数: " + bubbleSort.getSwapCount());
        });
        TestHelper.check("冒泡排序优化思路1测试", () -> {
            BubbleSort_1<Integer> bubbleSort_1 = new BubbleSort_1<>();
            bubbleSort_1.sort(ascExamples);
        });
        TestHelper.check("冒泡排序优化思路2测试", () -> {
            BubbleSort_2<Integer> bubbleSort_2 = new BubbleSort_2<>();
            bubbleSort_2.sort(ascExamples);
        });
        ExamplesHelper.println(ascExamples);

        System.out.println("----------------如果数组尾部局部有序,那么可以进行优化----------------");
        TestHelper.check("冒泡排序优化思路2测试", () -> {
            BubbleSort_1<Integer> bubbleSort_1 = new BubbleSort_1<>();
            bubbleSort_1.sort(tailAscOrderExamples);
        });
        TestHelper.check("冒泡排序优化思路2测试", () -> {
            BubbleSort_1<Integer> bubbleSort_1 = new BubbleSort_1<>();
            bubbleSort_1.sort(tailAscOrderExamples1);
        });
        ExamplesHelper.println(tailAscOrderExamples1);
    }

    public static void SelectSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        TestHelper.check("原始选择排序测试", () -> {
            assert examples != null;
            SelectSort<Integer> selectSort = new SelectSort<>();
            selectSort.sort(examples);
            System.out.println("比较次数: " + selectSort.getCmpCount());
            System.out.println("交换次数: " + selectSort.getSwapCount());
        });
        ExamplesHelper.println(examples);
    }

    public static void HeapSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        TestHelper.check("原始堆排序(优化后选择排序)测试", () -> {
            assert examples != null;
            HeapSort<Integer> heapSort = new HeapSort<>();
            heapSort.sort(examples);
            System.out.println("比较次数: " + heapSort.getCmpCount());
            System.out.println("交换次数: " + heapSort.getSwapCount());
        });
        ExamplesHelper.println(examples);
    }

    public static void InsertSortTest(){
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        Integer[] examples1 = ExamplesHelper.copy(examples);
        Integer[] examples2 = ExamplesHelper.copy(examples);
        TestHelper.check("原始插入排序测试", () -> {
            assert examples != null;
            InsertSort<Integer> insertSort = new InsertSort<>();
            insertSort.sort(examples);
            System.out.println("比较次数: " + insertSort.getCmpCount());
            System.out.println("交换次数: " + insertSort.getSwapCount());
        });
        ExamplesHelper.println(examples);
        System.out.println("----------------将交换转化为挪动,那么可以进行优化----------------");
        TestHelper.check("插入排序优化思路1测试", () -> {
            InsertSort_1<Integer> insertSort_1 = new InsertSort_1<>();
            insertSort_1.sort(examples1);
            System.out.println("比较次数: " + insertSort_1.getCmpCount());
        });
        ExamplesHelper.println(examples1);
        System.out.println("----------------查找插入位置的转化为二分搜索,那么可以进行优化----------------");
        TestHelper.check("插入排序优化思路2测试", () -> {
            InsertSort_2<Integer> insertSort_2 = new InsertSort_2<>();
            insertSort_2.sort(examples2);
            System.out.println("比较次数: " + insertSort_2.getCmpCount());
        });
        ExamplesHelper.println(examples2);
    }

    public static void MergeSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        TestHelper.check("原始归并排序测试", () -> {
            assert examples != null;
            MergeSort<Integer> mergeSort = new MergeSort<>();
            mergeSort.sort(examples);
            System.out.println("比较次数: " + mergeSort.getCmpCount());
        });
        ExamplesHelper.println(examples);
    }

    private static void QuickSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        Integer[] ascExamples = ExamplesHelper.ascOrder(0, 10000);

        TestHelper.check("原始快速排序测试", () -> {
            assert examples != null;
            QuickSort<Integer> quickSort = new QuickSort<>();
            quickSort.sort(examples);
            System.out.println("比较次数: " + quickSort.getCmpCount());
        });
        ExamplesHelper.println(examples);
        System.out.println("----------------如果序列接近有序时快排会最坏表现,那么可以进行优化----------------");
        TestHelper.check("序列默认有序下快速排序测试", () -> {
            QuickSort<Integer> quickSort = new QuickSort<>();
            quickSort.sort(ascExamples);
            System.out.println("比较次数: " + quickSort.getCmpCount());
        });
        TestHelper.check("快速排序优化思路1测试", () -> {
            QuickSort_1<Integer> quickSort_1 = new QuickSort_1<>();
            quickSort_1.sort(ascExamples);
            System.out.println("比较次数: " + quickSort_1.getCmpCount());
            System.out.println("交换次数: " + quickSort_1.getSwapCount());
        });
    }

    private static void ShellSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        TestHelper.check("原始希尔排序测试", () -> {
            assert examples != null;
            ShellSort<Integer> shellSort = new ShellSort<>();
            shellSort.sort(examples);
            System.out.println("比较次数: " + shellSort.getCmpCount());
            System.out.println("交换次数: " + shellSort.getSwapCount());
        });
        ExamplesHelper.println(examples);
    }

    private static void CountingSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        Integer[] examples1 = ExamplesHelper.copy(examples);
        TestHelper.check("原始计数排序测试", () -> {
            assert examples != null;
            CountingSort countingSort = new CountingSort();
            countingSort.sort(examples);
        });
        ExamplesHelper.println(examples);
        TestHelper.check("计数排序优化思路1测试", () -> {
            CountingSort_1 countingSort_1 = new CountingSort_1();
            countingSort_1.sort(examples1);
        });
        ExamplesHelper.println(examples1);
    }

    private static void RadixSortTest() {
        Integer[] examples = ExamplesHelper.random(10000,0,10000);
        Integer[] examples1 = ExamplesHelper.copy(examples);
        TestHelper.check("原始基数排序测试", () -> {
            assert examples != null;
            RadixSort radixSort = new RadixSort();
            radixSort.sort(examples);
        });
        ExamplesHelper.println(examples);
        TestHelper.check("基数排序的另一种思路测试", () -> {
            RadixSort_1 radixSort_1 = new RadixSort_1();
            radixSort_1.sort(examples1);
        });
        ExamplesHelper.println(examples1);
    }

    private static void BucketSortTest() {
        Double[] examples = {0.34,0.47,0.29,0.84,0.45,0.38,0.35,0.76};
        TestHelper.check("原始桶排序测试", () -> {
            BucketSort bucketSort = new BucketSort();
            bucketSort.sort(examples);
        });
        System.out.println(Arrays.toString(examples));
    }

    public static void main(String[] args) {
        /*比较排序*/
//        BubbleSortTest();   //冒泡排序测试
//        SelectSortTest();   //选择排序测试
//        HeapSortTest();   //堆排序测试
//        InsertSortTest();   //插入排序测试
//        MergeSortTest();    //归并排序测试
//        QuickSortTest();    //快速排序测试
//        ShellSortTest();    //希尔排序测试
        /*非比较排序*/
//        CountingSortTest(); //计数排序测试
//        RadixSortTest();    //基数排序测试
        BucketSortTest();   //桶排序测试
    }

}
