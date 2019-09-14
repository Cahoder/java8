package foundation.methodRefer;

import foundation.Person;

import java.lang.String;

/**
 * 定义一个构造器方法引用的接口
 */
public interface ConstructorMethodReferInterface {
    Person createPersonClass(char mySex, int myAge, String myName);
}
