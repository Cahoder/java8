package algorithm.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 1959年由唐纳德·希尔提出
 * 最原始的希尔排序,时间O(),空间O()
 * 随着每次步长step的缩小,逆序对的数量也在减少
 * 因此希尔排序底层一般使用插入排序,因为逆序对的数量越少,插入排序的时间复杂度越低
 * 不稳定的排序算法
 */
public class ShellSort<E extends Comparable<E>> extends Sort<E>{

    @Override
    protected void sort() {
//        List<Integer> stepSequences = ShellStepSequences();
        List<Integer> stepSequences = SedgeWickStepSequences();
        for (Integer step : stepSequences) {
            sort(step);
        }
    }

    /**
     * 根据不同的步长分成列进行(插入)排序
     * @param step 步长
     */
    private void sort(int step) {
        //col表示对第i列上的元素进行排序
        for (int col = 0; col < step; col++) {
            //插入排序实现,第col列第row行的元素在数组中的索引 = col+row*step
            for (int begin = col+step; begin < array.length; begin += step) {
                int cur = begin;
                while (cur > col && cmp(cur,cur-step) < 0) {
                    swap(cur,cur-step);
                    cur -= step;
                }
            }
        }
    }

    /**
     * @return 希尔本人推荐的步长序列
     *          最坏时间复杂度O(n^2)
     */
    private List<Integer> ShellStepSequences() {
        List<Integer> stepSequences = new ArrayList<>();
        int step = array.length;
        while ((step >>= 1) > 0) {
            stepSequences.add(step);
        }
        return stepSequences;
    }

    /**
     * @return 1986年由Robert SedgeWick提出的步长序列
     *          最坏时间复杂度O(n^(4/3))
     */
    private List<Integer> SedgeWickStepSequences() {
        List<Integer> stepSequences = new ArrayList<>();
        int k = 0, step = 0;
        while (true) {
            //如果k是偶数
            if (k%2 == 0) {
                int pow = (int) Math.pow(2,k>>1);
                step = 1 + 9*(pow*pow - pow);
            }
            //如果k是奇数
            else {
                int pow1 = (int) Math.pow(2,(k-1)>>1);
                int pow2 = (int) Math.pow(2,(k+1)>>1);
                step = 1 + 8*pow1*pow2 - 6*pow2;
            }
            if (step >= array.length) break;
            stepSequences.add(0,step);
            k++;
        }

        return stepSequences;
    }

}
