package apiSuper;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 函数式接口
 * JDK1.8++
 * @see java.util.function
 * 大致分为四类函数式接口俄
 * Consumer 消费型
 * Supplier 供给型
 * Function 函数型
 * Predicate 断定型
 */
public class FunctionInterfaceClass {

    //函数型函数式接口: 指定输入类型和输出类型
    public int functionDemo(Character character){
        Function<Character, Integer> function = new Function<Character, Integer>() {
            @Override
            public Integer apply(Character character) {
                return character - '0';
            }
        };
        //lambda简化: Function<Character, Integer> function = character1 -> character1 - '0';
        return function.apply(character);
    }

    //断定型函数式接口: 判断输入类型是否符合个人要求
    public boolean predicateDemo(String str) {
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.matches("[0-9]+");
            }
        };
        //lambda简化: Predicate<String> predicate = s -> s.matches("[0-9]+");
        return predicate.test(str);
    }

    //消费型函数式接口: 对输入的参数进行个性化处理
    public void consumerDemo(int[] ints){
        Consumer<int[]> consumer = new Consumer<int[]>() {
            @Override
            public void accept(int[] integers) {
                System.out.println(Arrays.toString(integers));
            }
        };
        //lambda简化: Consumer<int[]> consumer = integers -> System.out.println(Arrays.toString(integers));

        consumer.accept(ints);
    }

    //供给型函数式接口:
    public int[] supplierDemo(int numberSize){
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return (int) (Math.random() * 100);
            }
        };
        //lambda简化: Supplier<Integer> supplier = () -> (int) (Math.random() * 100);
        int[] rst = new int[numberSize];
        for (int i = 0; i < rst.length; i++) rst[i] = supplier.get();

        return rst;
    }
}
