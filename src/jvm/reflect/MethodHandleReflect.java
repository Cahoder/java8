package jvm.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * 现代化反射API-字节码层面反射调用
 * JDK1.7+ support
 * @version 1.0
 * @since 2023/3/29
 **/
public class MethodHandleReflect {

    public static void main(String[] args) {
        try {
            //Lookup是MethodHandle的创建工厂
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            //MethodType代表方法签名(入参列表\返回值等描述)
            MethodType voidMethodType = MethodType.methodType(void.class);
            //MethodHandle负责方法句柄调用
            MethodHandle voidConstructor = lookup.findConstructor(MethodHandleReflect.class, voidMethodType);
            Object instance1 = voidConstructor.invoke();
            System.out.println(instance1);

            MethodType strMethodType = MethodType.methodType(void.class, String.class);
            MethodHandle strConstructor = lookup.findConstructor(MethodHandleReflect.class, strMethodType);
            Object instance2 = strConstructor.invoke("you");
            System.out.println(instance2);

            MethodHandle nameField = lookup.findGetter(MethodHandleReflect.class, "name", String.class);
            System.out.println(nameField.invoke(instance1));

            MethodType getterMethodType = MethodType.methodType(String.class);
            MethodHandle getterMethod = lookup.findVirtual(MethodHandleReflect.class, "getName", getterMethodType);
            System.out.println(getterMethod.invoke(instance2));

            Method privateMethod = MethodHandleReflect.class.getDeclaredMethod("learnPrograming", String.class);
            privateMethod.setAccessible(true);
            MethodHandle privateMethodHandle = lookup.unreflect(privateMethod);
            privateMethodHandle.invoke(instance1, "Java");

            MethodType staticMethodType = MethodType.methodType(String.class, String.class);
            MethodHandle publicStaticMethod = lookup.findStatic(MethodHandleReflect.class, "declaration", staticMethodType);
            System.out.println(publicStaticMethod.invoke("庄子"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private String name = "hello world";

    public MethodHandleReflect() {

    }

    public MethodHandleReflect(String name) {
        this.name = this.name + " " + name;
    }

    public String getName() {
        return name;
    }

    private void learnPrograming(String lang) {
        System.out.println("I am learning " + lang);
    }

    public static String declaration(String author) {
        return author + ": " + "吾生也有涯,而知也无涯.";
    }

    @Override
    public String toString() {
        return "MethodHandleTest{" +
                "name='" + name + '\'' +
                '}';
    }
}
