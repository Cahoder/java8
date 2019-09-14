package apiSuper;

//单列集合
//集合接口属于所有的集合的超类，但是又是继承于 Iterable 接口


import java.lang.String;
import java.util.*;
import foundation.Person;

public class CollectionClass {

    void superCollection(){
        Collection<String> collection1 = new ArrayList<>();     //将一个具体类赋予父类实现多态
        Collection<String> collection2 = new HashSet<>();     //将一个具体类赋予父类实现多态

        //集合的提供的共性方法
        collection1.add("What is the problem ?");
        collection1.add("What is the problem ?");
        collection1.add("What is the problem ?");
        collection1.add("What is the problem ?"); //允许重复元素

        collection2.add("What is the problem ?");
        collection2.add("What is the problem ?");
        collection2.add("What is the problem ?");
        collection2.add("What is the problem ?"); //不允许重复元素

        collection1.remove("What is the problem ?");
        System.out.println(collection1);
        System.out.println(collection2);
        System.out.println("================================");

        boolean b = collection1.contains("What is the problem ?");
        int c = collection1.size();
        boolean d = collection1.isEmpty();

        //集合的遍历
        System.out.println("集合的遍历方法一");
        iteratotFor(collection1);
        System.out.println("集合的遍历方法二");
        foreach(collection1);
    }

    void collectionUtils(){
        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList,"i","am","a","good","boy");
        System.out.println(arrayList);
        Collections.shuffle(arrayList);
        System.out.println(arrayList);
        Collections.sort(arrayList);  //默认ASC排序
        System.out.println(arrayList);

        System.out.println("\n-------------自定义排序----------------");
        ArrayList<Person> personArrayList = new ArrayList<>();
        Person p1 = new Person('男',18,"彩虹的");
        Person p2 = new Person('女',15,"哇嘎厉害");
        Person p3 = new Person('女',12,"b法大赛父");
        Person p4 = new Person('女',12,"a农垦经济");
        Collections.addAll(personArrayList,p1,p2,p3,p4);

        System.out.println("写法一：comparable  (this和对方参数比较，需要实现comparable接口的compareTo方法)");
        Collections.sort(personArrayList);
        System.out.println(personArrayList);

        Collections.shuffle(personArrayList);  //打乱


        System.out.println("\n写法二：comparator  (找一个第三方的裁判,比较两个元素的参数)");
        Collections.sort(personArrayList, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                //return 0;  //默认0表示默认需要排序的元素是相同的，无序排序
                //return o1.getMyAge() - o2.getMyAge(); // 参数一 - 参数二  ==>  表示升序排序
                //return o2.getMyAge() - o1.getMyAge();  // 参数二 - 参数一   ==>  表示降序排序
                int result = o2.getMyAge()-o1.getMyAge();
                //组合多种排序手段
                if (result == 0) { result = o1.getMyName().charAt(0) - o2.getMyName().charAt(0); }
                return result;
            }
        });
        //lambda写法作为方法的返回值
        /*Collections.sort(personArrayList, (o1, o2) -> {
            //return 0;  //默认0表示默认需要排序的元素是相同的，无序排序
            //return o1.getMyAge() - o2.getMyAge(); // 参数一 - 参数二  ==>  表示升序排序
            //return o2.getMyAge() - o1.getMyAge();  // 参数二 - 参数一   ==>  表示降序排序
            int result = o2.getMyAge()-o1.getMyAge();
            //组合多种排序手段
            if (result == 0) { result = o1.getMyName().charAt(0) - o2.getMyName().charAt(0); }
            return result;
        });*/

        System.out.println(personArrayList);
    }


    //方法一 ：原生方法
    //使用 Collection 的 Iterator iterator() 方法进行遍历
    //而且只能够打印元素 不可以增删修改元素
    private static void iteratotFor(Collection collection){
        Iterator it = collection.iterator();  //此时指针位于集合的 -1 位置

        while (it.hasNext()){
            System.out.println(it.next());
        }
    }

    //方法二：增强for方法
    //底层原理还是对方法一的封装
    //增强for循环仅仅适用于实现了Iterable接口的类型： 数组和集合
    //而且只能够打印元素 不可以增删修改元素
    private static <E> void foreach(Collection<E> coll){
        for (E i: coll) {
            System.out.println(i);
        }
    }

    //Link接口属于Collection的一个子类
    //包含三个重要的link集合
    public <E> void superLink(){
        //数组结构  多线程
        ArrayList<E> arrayList = new ArrayList<>();
        //双向链表  多线程
        LinkedList<E> linkedList = new LinkedList<>();
        //数组结构  单线程
        Vector<E> vector = new Vector<>();

    }

    //Set接口属于Collection的一个子类
    //包含三个重要的set集合
    public <E> void superSet(E obj){
        //多线程 无序
        //jdk<1.8  hashSet集合的数据结构（哈希表） ： 数组+链表
        //jdk>=1.8  hashSet集合的数据结构（哈希表） ： 数组+链表   ||   数组+红黑树(当链表的长度超过了8位，将会被转成红黑树)
        HashSet<E> hashSet = new HashSet<>();
        /*
        HashSet<E>.add() 的原理
            1. 首先进行哈希值计算  HashSet<E> int hashCode()
            2. 在数组哈希表中查找是否有相同的哈希值
            if(有相同的哈希值) //即是发生了哈希碰撞 就会再调用 E.equals() 方法判断是否是相同的元素
               if(equals() == true) 丢弃元素不存储
               else if (equals() == false) 存储元素
            else if(无相同的哈希值) 存储元素

        注：存储自定义的对象需要重写hashCode()  和 equals() 方法，否则就会一直可以存储相同的元素
        */
        hashSet.add(obj);

        //多线程 有序的HashSet
        //LinkedHashSet集合的数据结构（哈希表：数组+链表/红黑树） + 链表
        LinkedHashSet<E> linkedHashSet = new LinkedHashSet<>();


        TreeSet<E> treeSet = new TreeSet<>();

    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
