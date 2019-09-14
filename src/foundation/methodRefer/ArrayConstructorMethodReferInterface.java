package foundation.methodRefer;

/**
 * 定义一个数组构造器的方法引用接口
 */
@FunctionalInterface
public interface ArrayConstructorMethodReferInterface {
    int[] createNewArray(int arrayLen);
}
