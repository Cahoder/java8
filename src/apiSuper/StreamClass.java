package apiSuper;

import java.lang.String;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 * Stream流式思想
 * JDK1.8++
 * 流是一个来自数据源（集合，数组）的元素队列
 * 流并不会存储元素，而是按需计算
 *
 * 自带的基础特征
 *  1.Pipelining : 中间操作都会返回流本身，这样多个操作可以串成一个管道，以便对操作进行优化，比如延迟执行和短路
 *
 *  2.内部迭代： 不同于集合遍历都是通过Iterator或者增强for的方式，显式的在集合外部进行迭代，这叫作 “外部迭代”
 *           Stream 提供了内部迭代的方式，流可以直接调用遍历方法
 *
 *  3.每个流对象允许操作一次，返回新的流对象，原流对象就会被关闭
 *
 * 操作步骤：
 * 1.获取数据源（数组，集合）
 * 2.数据转换 -> 流对象的元素队列
 * 3.执行操作获取想要的结果，每次操作不改变原有的流对象，而是返回一个新的流对象 （多次操作就像链条一样排列，最终串成一个管道）
 */
public class StreamClass {
    //对比原始方法对集合进行过滤和流式思想对流进行过滤
    public void StreamFiltter(){
        //A:原始方法对集合进行过滤
        /*
         * ( 循环-> 方式 + 过滤 -> 目的 )
         */
        ArrayList<String> rawData = new ArrayList<>();
        rawData.add("张三");
        rawData.add("张无忌");
        rawData.add("张三丰");
        rawData.add("周芷若");
        rawData.add("赵敏");
        rawData.add("张强");

        ArrayList<String> filterResult = primaryFilter(rawData,
                (name)-> name.startsWith("张"),
                (name)-> name.length() == 3
        );
        //外部迭代
        for (String s : filterResult) {
            System.out.println(s);
        }



        //B:流式思想方法对集合进行过滤

        rawData.stream()               //这时候集合就变成了一个元素队列
                .filter((name)-> name.startsWith("张"))
                .filter((name)-> name.length() == 3)
                .forEach(System.out::println);          //内部迭代

    }

    //获取流的两种方式
    public void ObtainStream(){
        // A.Collection 接口中有一个默认方法 stream()
        List<String> lists = new ArrayList<>();
        Stream<String> stream1 = lists.stream();

        Set<String> sets = new HashSet<>();
        Stream<String> stream2 = sets.stream();

        //双列集合必须转成单列集合之后才可以转换成流对象
        Map<String,String> maps = new HashMap<>();
        Set<String> keys = maps.keySet();    //获取Map的所有 “键” 形成集合
        Stream<String> stream3 = keys.stream();
        Collection<String> values = maps.values();     //获取Map的所有 “值” 形成集合
        Stream<String> stream4 = values.stream();
        Set<Map.Entry<String,String>> entries = maps.entrySet();       //获取Map的所有 “键值映射关系” 形成集合
        Stream<Map.Entry<String, String>> stream5 = entries.stream();


        //B.调用Stream 接口中有一个默认方法 of()
        Stream<String> stream6 = Stream.of("f","u","c","k");
        Stream<String> stream7 = Stream.of(new String[]{"f","u","c","k"});
    }


    /***流中的常用方法，大体上分为两类
     *
     * 1.延迟方法  filter map limit skip concat
     * 2.终结方法(只有两个) foreach count
     */
    public void StreamOperation(){
        ArrayList<String> listsA = new ArrayList<>(Arrays.asList("张三", "张无忌", "张三丰", "周芷若", "赵敏", "张强", "张大三", "张海峰", "张口"));
        ArrayList<Integer> listsB = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));


        listsA.stream()
                .filter((String element)->{return element.startsWith("张");})
                .limit(5).skip(1).forEach(System.out::println);



        System.out.println("------------------华丽的分割线-----------------------");
        long counts = listsB.stream().map(new Function<Integer, String>() {
            @Override
            public String apply(Integer i) {
                return Integer.toString(i);
            }
        }).count();
        System.out.println(counts);



        System.out.println("------------------华丽的分割线-----------------------");
        Stream<String> concats = Stream.concat(listsA.stream(),listsB.stream().map((Integer i)->{return Integer.toString(i);}));
        concats.forEach(System.out::println);
    }

    @SafeVarargs
    //原始方法对集合进行过滤
    private final <T> ArrayList<T> primaryFilter(ArrayList<T> lists, Predicate<T>... conditions){

        ArrayList<T> filberts = new ArrayList<>();
        for (T list : lists) {
            boolean test = true;
            for (Predicate<T> condition : conditions) {
                test = condition.test(list);
                if (!test) break;
            }
            if (test) filberts.add(list);
        }
        return filberts;
    }

}
