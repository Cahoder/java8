package apiSuper;

//定一个带有泛型的接口 如不指定 则默认为 Object 类型

public interface GenericsInterface<I> {
    default void defaultMethod(){
        System.out.println("defaultMethod can have a method body only in the interface !");
    };
    void setVariable(I i);
}
