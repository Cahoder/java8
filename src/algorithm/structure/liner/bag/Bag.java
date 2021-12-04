package algorithm.structure.liner.bag;

import algorithm.structure.liner.list.ArrayListPro;

import java.util.*;

/**
 * 背包 - 只进不出
 * @param <E>
 */
@SuppressWarnings("all")
public class Bag<E> implements Iterable<E> {
    /**
     * 定一个集合存储背包中的元素
     */
    private ArrayListPro<E> bags = new ArrayListPro<>();

    /**
     * 创建一个空背包
     */
    public Bag() {}

    /**
     * @param e 往背包中添加元素
     */
    public void add(E e) {
        bags.add(e);
    }

    /**
     * @return 背包中的元素数量
     */
    public int size() {
        return bags.size();
    }

    /**
     * @return 背包是否为空
     */
    public boolean isEmpty() {
        return bags.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        //return new BagIterator();
        return new RandomBagIterator();
    }

    /*默认的迭代器*/
    private class BagIterator implements Iterator<E> {
        protected int index = 0;

        public BagIterator() {}

        @Override
        public boolean hasNext() {
            return index != size();
        }

        @Override
        public E next() {
            return bags.get(index++);
        }
    }

    /*可以随机访问元素的迭代器*/
    private class RandomBagIterator extends BagIterator {
        private int[] seq;

        public RandomBagIterator() {
            super();
            seq = new int[size()];
            for (int i = 0; i < seq.length; i++) seq[i] = i;

            /*shuffle seq start*/
            Random rand = new Random();
            for ( int i = seq.length; i > 0; i-- ){
                int randInd = rand.nextInt(i);
                int temp = seq[randInd];
                seq[randInd] = seq[i-1];
                seq[i-1] = temp;
            }
            /*shuffle seq end*/
        }

        @Override
        public E next() {
            return bags.get(seq[index++]);
        }
    }
}
