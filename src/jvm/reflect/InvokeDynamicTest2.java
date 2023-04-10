package jvm.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * JDK1.7前只能用ASM等字节码工具直接生成字节码调用
 **/
public class InvokeDynamicTest2 {

    public static void main(String[] args) {
        (new InvokeDynamicTest2().new Son()).thinking();
    }

    class GrandFather {
        void thinking() {
            System.out.println("i am grandfather");
        }
    }
    class Father extends GrandFather {
        @Override
        void thinking() {
            System.out.println("i am father");
        }
    }
    class Son extends Father {
        @Override
        void thinking() {
            // 请读者在这里填入适当的代码（不能修改其他地方的代码）
            // 实现调用祖父类的thinking()方法，打印"i am grandfather"
            try {
                //super关键字无法调用GrandFather
                MethodType mt = MethodType.methodType(void.class);
                Field lookupImpl = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                lookupImpl.setAccessible(true);
                MethodHandle mh = ((MethodHandles.Lookup) lookupImpl.get(null)).findSpecial(GrandFather.class, "thinking", mt, Father.class);
                mh.invoke(this);
            } catch (Throwable t) {
                //ignored
            }
        }
    }

}
