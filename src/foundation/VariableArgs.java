package foundation;

import java.lang.String;

/**
 * 可变参数
 * JDK>=1.5
 * 使用前提 数据类型确定相同 但是参数个数不确定
 * 原理：可变参数底层就是一个数组，根据传递的参数不同创建不同长度的数组
 *
 */

class VariableArgs {

    /* 可变参数注意事项
     * 1.一个方法里面只能够有一个可变参数
     * 2.可变参数只能写在参数列表的末尾
     * 3.想要输入不同类型的可变参数，可能用可变参数的终极写法 Object ... args
     */
    static void argsInput(String str, Object... args){
        System.out.println("传递的参数数组的长度是："+args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("输入的参数"+(i+1)+"为："+args[i]);
        }
    }

}
