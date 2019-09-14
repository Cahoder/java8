package foundation;

import foundation.methodRefer.ArrayConstructorMethodReferInterface;
import foundation.methodRefer.ClassMethodReferInterface;
import foundation.methodRefer.ConstructorMethodReferInterface;
import foundation.methodRefer.ObjectMethodReferInterface;

import java.lang.String;

/**
 * 方法引用：想要实现一个接口中的抽象方法，但是实现该方法的步骤已经被其他地方实现过了，就可以进行方法引用
 *
 * 使用格式:
 *      第一种  ->   引用对象::对象的成员方法
 *      第二种  ->   引用类::类的静态成员方法
 *      第三种  ->   super父类::父类的成员方法
 *      第四种  ->   this本类::本类的成员方法
 *      第五种  ->   类构造器名::new
 *      第六种  ->   数组构造器名::new
 *
 * 注：抽象方法如有参数，该参数必须是引用方法中可以接收的参数类型，否则会抛出异常
 *    抽象方法如有返回类型，该返回类型必须是引用方法中会返回的类型，否则会抛出异常
 */

public class MethodReferenceClass {

    public void MethodReferenceClass(){

    }


    /**
     * 方法引用实例
     */
    public void operateMethodReference(){
        //对象的lambda写法
        objectMethodReference("我是lambda使用实例！",(String desc)->{
            System.out.println(desc);
        });

        //对象的方法引用优化lambda写法
        objectMethodReference("我是对象方法引用实例！",System.out::println);

        //类的方法引用优化lambda写法
        classMethodReference("我是类的静态方法引用实例！",MethodReferenceClass::printClassMethodReferStatic);

        //this类的成员方法的方法引用优化lambda写法
        classMethodReference("我是this的成员方法的方法引用实例！",new MethodReferenceClass()::printClassMethodRefer);

        //构造器的方法引用优化lambda写法
        Person person = constructorMethodReference('男', 18, "小靓仔", Person::new);
        System.out.println(person);

        //数组的方法引用优化lambda写法
        int[] ints = arrayconstructorMethodReference(5, int[]::new);
        System.out.println("数组的长度是：" + ints.length);


    }

    private static void objectMethodReference(String desc, ObjectMethodReferInterface omri){
        omri.printObjectMethodRefer(desc);
    }

    private static void classMethodReference(String desc, ClassMethodReferInterface cmri){
        cmri.printClassMethodRefer(desc);
    }

    private static Person constructorMethodReference(char mySex, int myAge, String myName, ConstructorMethodReferInterface cmri){
       return cmri.createPersonClass(mySex,myAge,myName);
    }

    private static int[] arrayconstructorMethodReference(int arrayLength, ArrayConstructorMethodReferInterface acmri){
        return acmri.createNewArray(arrayLength);
    }


    //使用本类的成员方法去实现类的方法引用
    private void printClassMethodRefer(String desc){
        System.out.println(desc);
    }

    //使用本类的静态方法去实现类的方法引用
    private static void printClassMethodReferStatic(String desc){
        System.out.println(desc);
    }

}
