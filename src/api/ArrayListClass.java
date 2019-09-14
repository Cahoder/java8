package api;

import java.lang.String;
import java.util.ArrayList;

// ArrayList 继承于 -> list接口 -> Collection接口

public class ArrayListClass {

    public ArrayList<String> getArrayListString(){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        list.add("My");
        list2.add("name");
        list2.add("is");
        list.addAll(list2);
        list.add("You");
        list.add("123");

        list.remove(list.get(4));
        System.out.println("集合的长度是:"+list.size());
        System.out.println(list);

        return list;
    }

    public ArrayList<Integer> getArrayListInteger(){
        ArrayList<Integer> list = new ArrayList<>();
        createArrayListInteger(list);
        System.out.println(list);
        return list;
    }


    //注意 ： 一切的泛型都是没有继承的概念的 》》》 例如： ArrayList<Integer>  ！==   ArrayList<Object>
    private ArrayList<Integer> createArrayListInteger(ArrayList<Integer> list){
        for (int i = 0 ; i<11; i++){
            list.add(i);
        }
        return list;
    }

}
