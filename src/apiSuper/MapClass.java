package apiSuper;

//双列集合


import java.lang.String;
import java.util.*;

import foundation.Person;

/**
 * Map<key, value>集合的特点
 * 1. key 不可以重复，value 可以重复
 * 2. key 和 value 的数据类型可以相同也可以不同
 * 3.底层是哈希表
 */
public class MapClass {


    public void MapOperation(){
        Map<Integer, String> map = new HashMap<>();
        map.put(0,"I am a good boy !");
        map.put(1,"I am a good girl !");
        map.put(4,"I am a good brother !");
        map.put(5,"I am a good sister !");
        map.put(2,"I am a good father !");
        map.put(3,"I am a good mother !");


        System.out.println("key的值为0对应的value为：" + map.get(0));
        System.out.println("移除指定的key的键值对：" + map.remove(0));
        System.out.println("判断是否包含指定的key的布尔结果为：" + map.containsKey(0));
        System.out.println("判断是否包含指定的value的布尔结果为：" + map.containsValue("I am a good mother !"));


        //遍历获取value
        Set<Integer> set = map.keySet();
        Iterator<Integer> it = set.iterator();
        //方法一
        for (Integer key : set) {
            String value = map.get(key);
            System.out.println(value);
        }
        //方法二
        while (it.hasNext()){
            Integer key = it.next();   //应该避免使用基本数据类型进行接收，避免出现空指针异常
            String value = map.get(key);
            System.out.println(value);
        }
        //方法三
        Set<Map.Entry<Integer, String>> sets = map.entrySet();
        for (Map.Entry<Integer,String> entry: sets) {
            System.out.println(entry.getKey() + "." + entry.getValue());

        }



        HashMapCustom();
        LinkedHashMapCustom();
        HashTableCustom();

    }

    private void HashMapCustom(){
        System.out.println("\n============存储自定义的类型，该类型必须重写equals()和hashCode()方法============");

        //string类内部已经重写了equals()和hashCode()方法
        HashMap<String,Person> customMap = new HashMap<>();
        customMap.put("string1",new Person('男',18,"彩虹的"));
        customMap.put("string2",new Person('女',18,"六六六"));
        customMap.put("string3",new Person('女',18,"七七起"));
        customMap.put("string4",new Person('女',18,"七七起"));
        Set<Map.Entry<String, Person>> entries = customMap.entrySet();
        for (Map.Entry<String, Person> entry : entries) {
            System.out.println(entry.getKey()+"==>"+entry.getValue());
        }

        //Person类内部已经重写了equals()和hashCode()方法
        HashMap<Person,String> customMap2 = new HashMap<>();
        customMap2.put(new Person('男',18,"彩虹的"),"hhh");
        customMap2.put(new Person('女',18,"六六六"),"hhh");
        customMap2.put(new Person('男',18,"七七起"),"hhh");
        customMap2.put(new Person('男',18,"七七起"),"hhh");
        Set<Map.Entry<Person, String>> entries1 = customMap2.entrySet();
        for (Map.Entry<Person, String> entry : entries1) {
            System.out.println(entry.getKey()+"==>"+entry.getValue());
        }
    }

    private void LinkedHashMapCustom(){
        System.out.println("\n===========LinkedHashMap和HashMap=============");
        //底层hashMap+链表
        //有序
        LinkedHashMap<Integer,String> linked = new LinkedHashMap<>();
        linked.put(0,"AAA");
        linked.put(1,"BBB");
        linked.put(2,"CCC");
        linked.put(2,"DDD");
        System.out.println(linked);

        //无序
        HashMap<Integer,String> map = new HashMap<>();
        map.put(2,"EEE");
        map.put(1,"FFF");
        map.put(0,"KKK");
        map.put(2,"LLL");
        System.out.println(map);
    }

    /**
     * java.util.HashTable<k,v> implements Map<k,v>
     *     JDK1.0
     *     单线程，速度慢
     *     不能存储null键和null值
     *     底层也是一个哈希表
     *
     */
    private void HashTableCustom(){
        Hashtable<Integer,String> table = new Hashtable<>();
        table.put(null,null); //Exception in thread "main" java.java.lang.NullPointerException
    }
}
