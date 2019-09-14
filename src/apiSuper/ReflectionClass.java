package apiSuper;

import foundation.Person;

import java.lang.reflect.*;
import java.lang.String;
import java.util.List;
import java.util.Map;

/**
 * 反射机制
 */
public class ReflectionClass {

    //获取class对象的方式，一个对象只能够有一个对应的class对象
    //一切对象都有class对象，包括（class，interface，enum，annotation，基本数据类型，void）
    //注：数组的class对象和数组的长度无关，和数组的维度和类型有关
    public void getClassObject(){
        /**
         * 获取class对象方法
         * 1.对象.getclass()
         * 2.对象.class
         * 3.Class.forName("path")
         */
        try {

            ReflectionClass rc = new ReflectionClass();
            Class aClass = rc.getClass();  //通过对象获取其class
            Class bClass = ReflectionClass.class;  //通过类获取其class
            Class cClass = Class.forName("apiSuper.ReflectionClass");   //通过动态加载类获取其class
            System.out.println("-----------每个class只能够有一个Class对象-----------");
            System.out.println(aClass.hashCode());
            System.out.println(bClass.hashCode());
            System.out.println(cClass.hashCode());
        } catch (ClassNotFoundException e) { e.printStackTrace(); }


        System.out.println("-----------数组的Class对象只和数组的维度和类型有关-----------");
        String[] arr00 = new String[10];
        int[] arr01 = new int[10];
        int[] arr02 = new int[30];
        int[][] arr03 = new int[10][5];
        System.out.println(arr00.getClass().hashCode());
        System.out.println(arr01.getClass().hashCode());
        System.out.println(arr02.getClass().hashCode());
        System.out.println(arr03.getClass().hashCode());

    }

    //通过反射机制获取类的具体信息（类名，成员属性，成员方法，构造器）
    public void getClassInfo(){
        try {
            Class<?> personClass = Class.forName("foundation.Person");
            //获取类名称
            String classNameAbsolute = personClass.getName();   //完整路径的类名称
            String className = personClass.getSimpleName();     //只有类名称
            System.out.println("类名："+ className);

            //获取类属性
            Field[] fieldsPublic = personClass.getFields();  //只能获得public的属性
            Field[] fieldsAll = personClass.getDeclaredFields();   //可以获得所有的属性
            Field field = personClass.getDeclaredField("mySex");
            for (Field field1 : fieldsAll) {
                System.out.println("属性："+field1);
            }

            //获取类方法
            Method[] methodsPublic = personClass.getMethods();  //只能获得public的方法
            Method[] methodsAll = personClass.getDeclaredMethods();  //可以获得所有的方法
            Method method = personClass.getDeclaredMethod("toString" /*,null*/ );     //null 可以省略
            Method overloadMethod = personClass.getDeclaredMethod("toString",String.class);   //在方法重载了的情况下可以给予第二个class参数类型区分
            for (Method method1 : methodsAll) {
                System.out.println("方法："+method1);
            }

            //获取类构造方法
            Constructor[] constructorsPublic = personClass.getConstructors(); //只能获得public的构造方法
            Constructor[] constructorsAll = personClass.getDeclaredConstructors(); //可以获得所有的构造方法
            Constructor constructor = personClass.getDeclaredConstructor( /*,null*/ );  //null 可以省略
            Constructor overLoadConstructor = personClass.getDeclaredConstructor(char.class,int.class,String.class);//在方法重载了的情况下可以给予class参数类型区分
            for (Constructor constructor1 : constructorsAll) {
                System.out.println("构造器："+constructor1);
            }

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) { e.printStackTrace(); }

    }

    private static void toto(String[] args){
        System.out.println("invoke static toto() !");
    }

    //通过反射api动态的操作：构造器，方法，属性
    public void classReflectOperate(){
        try {
            Class<?> personClass = Class.forName("foundation.Person");
            //调用无参数构造
            Person personA = (Person)personClass.newInstance();   //方式一 //默认调用无参构造器，所以建议所有的类都必须有一个无参构造
            System.out.println(personA);
            Constructor personConstructorA = personClass.getDeclaredConstructor(); //方式二 //使用获取构造器的方式再调用无参构造
            Person personB = (Person)personConstructorA.newInstance();
            //System.out.println(personB);

            //调用有参数构造
            Constructor personConstructorB = personClass.getDeclaredConstructor(char.class,int.class,String.class); //方式二 //使用获取构造器的方式再调用无参构造
            Person personC = (Person)personConstructorB.newInstance('男',18,"蔡宏德");
            System.out.println(personC);

            //反射操作方法
            Person personD = (Person)personClass.newInstance();
            Method setInfo = personClass.getDeclaredMethod("setInfo",String.class, char.class, int.class);
            setInfo.invoke(personD,"蔡小小",'男',20);   //等同于 personD.setInfo("蔡小小",'男',20);
            System.out.println(personD.toString());

            //反射操作属性
            Person personE = (Person)personClass.newInstance();
            Field myAge = personClass.getDeclaredField("myAge");
            myAge.setAccessible(true);
            myAge.set(personE,15);              //等同于 personE.setMyAge(15)
            System.out.println(myAge.get(personE));    //等同于 personE.getMyAge()

            //反射操作静态方法
            /** @see DynamicCompileClass#JavaCompilerExecute() */
            Class<ReflectionClass> classClass = ReflectionClass.class;
            Method toto = classClass.getDeclaredMethod("toto",String[].class);
            //静态方法可以不需要指定操作对象obj，等同于 toto.invoke(ReflectionClass,(Object) new String[]{})
            //如果不强转成Object类型会被编译成为可变参数 String...args 导致参数对不上抛出异常 IllegalArgumentException: wrong number of arguments
            toto.invoke(null, (Object) new String[]{});

            /*
              注：想要操作私有的方法/属性/构造器，必须设置setAccessible(true)
              发射运行耗时大概是普通方法操作的30倍，所以如果需要频繁操作反射，可以setAccessible(true)关闭安全检查
             */


        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射api动态的获取方法参数泛型信息
     * 由于泛型是在编译期间就会消失掉无法获取到泛型信息，所以java为反射api专门提供了获取泛型信息的方法
     */
    public void getReflectGenerics(){
        try {
            System.out.println("-----------------获取指定方法参数泛型信息-------------------");
            Method test01 = ReflectionClass.class.getDeclaredMethod("test01",Map.class,List.class);
            Type[] parameterTypes = test01.getGenericParameterTypes();
            for (Type parameterType : parameterTypes) {
                System.out.println("#"+parameterType);
                if (parameterType instanceof ParameterizedType){
                    Type[] genericTypes = ((ParameterizedType) parameterType).getActualTypeArguments();
                    for (Type genericType : genericTypes) {
                        System.out.println("泛型参数类型："+genericType);
                    }
                }
            }
            
            System.out.println("\n");

            System.out.println("-----------------获取指定方法返回泛型信息-------------------");
            Method test02 = ReflectionClass.class.getDeclaredMethod("test02");
            Type returnType = test02.getGenericReturnType();
            if (returnType instanceof ParameterizedType){
                Type[] genericTypes = ((ParameterizedType) returnType).getActualTypeArguments();
                for (Type genericType : genericTypes) {
                    System.out.println("泛型返回类型："+genericType);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    private void test01(Map<String,Person> map, List<ReflectionClass> list){}
    private Map<Integer,Person> test02(){return null;}



    @SuppressWarnings(value = {"all"})
    /**通过反射api动态的获取注解信息
     * @see     jdbc.Main#GetReflectionJavaBean()
     */
    public void getReflectAnnotaion(){}
}
