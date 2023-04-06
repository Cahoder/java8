package jvm.dispatch;

/**
 * java的静态分派和动态分派
 * 静态分派一般在编译期确定 - 例如: overload
 * 动态分派一般在执行器确定 - 例如: override
 *
 * 接收者指实际发生调用对象
 * Father father = new Son()     Son对象就是接收者
 *
 * 接收者和方法参数合称为宗量
 * 根据分派基于多少种宗量，可以将分派划分为单分派和多分派
 *
 * 单分派与多分派
 * 单分派是根据一个宗量对目标方法进行选择
 * 多分派则是根据多于一个宗量对目标方法进行选择
 *
 * java的静态分派 属于 多分派  -- 例如: overload是在编译期中多个同名方法根据静态类型确定
 * java的动态分派 属于 单分派  -- 例如: override无论怎么重写,其接收者只会由一个
 **/
public class Dispatch {
    static class QQ {}
    static class _360 {}
    public static class Father {
        public void hardChoice(QQ arg) {
            System.out.println("father choose qq");
        }
        public void hardChoice(_360 arg) {
            System.out.println("father choose 360");
        }
    }
    public static class Son extends Father {
        @Override
        public void hardChoice(QQ arg) {
            System.out.println("son choose qq");
        }
        @Override
        public void hardChoice(_360 arg) {
            System.out.println("son choose 360");
        }
    }
    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        //静态分派: 会产生两条invokevirtual指令指向常量池Father::hardChoice
        father.hardChoice(new _360());
        //动态分派: 依据接收者不同去虚方法表中搜索执行
        son.hardChoice(new QQ());
    }
}

