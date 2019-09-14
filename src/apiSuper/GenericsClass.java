package apiSuper;


import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//创建一个含有泛型的类 如果不指定数据类型 那么 E 默认为 Object 类型
public class GenericsClass<E> implements GenericsInterface<E> {

    private E selfAttr ;

    //将要传递的数据类型的权利移交给调用者
    public void setVariable(E e){
        this.selfAttr = e;
        System.out.println(this.selfAttr);
    }

    //创建一个含有泛型的方法 如果不指定数据类型 那么 T 默认为 Object 类型
    public static <T> void getGenericsMethod(T[] arr){
        for (T i: arr) {
            System.out.println(i);
        }
    }


    public void genericsWildcard(){
        ArrayList<String> al = new ArrayList<>();
        al.add("How");
        al.add("old");
        al.add("are");
        al.add("you");
        al.add("?");
        printCollection(al);
    }


    //创建一个含有未知泛型的方法 使用泛型的通配符 ? 方法  默认为 Object 类型
    private static void printCollection (Collection<?> coll){
        Iterator it = coll.iterator();   //等价于  Iterator<?> it = coll.iterator();
        while (it.hasNext()){
            Object i = it.next();
            System.out.println(i);
        }
    }


    //测试泛型的上下限
    public static void testGenericsWildcard(){
        Collection<Integer> integers = new ArrayList<>();   // === Collection<Integer> integers = new ArrayList<Integer>();
        Collection<Number> numbers = new ArrayList<>();   // === Collection<Number> numbers = new ArrayList<Number>();
        Collection<String> strings = new ArrayList<>();   // === Collection<String> strings = new ArrayList<String>();
        Collection<Object> objects = new ArrayList<>();   // === Collection<Object> objects = new ArrayList<Object>();

        getElement1(integers);   // Integer extends Number
        getElement1(numbers);    // Number == Number
//        getElement1(strings);    // String !extends Number  报错
//        getElement1(objects);    // Object > Number  报错

        getElement2(objects);    // Object super Number
        getElement2(numbers);    // Number == Number
//        getElement2(strings);    // String !super Number  报错
//        getElement2(integers);   // Integer < Number  报错


    }


    //泛型通配符的受上限  ? extends E    <==>   ? 的类型必须是E的子类/本身
    private static void getElement1(Collection< ? extends Number> coll){
        System.out.println(coll.isEmpty());
    }

    //泛型通配符的受下限  ? super E    <==>   ? 的类型必须是E的父类/本身
    private static void getElement2(Collection< ? super Number> coll){
        System.out.println(coll.isEmpty());
    }
}
