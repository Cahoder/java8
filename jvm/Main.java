package jvm;

import java.lang.String;

public class Main {
    public static void main(String[] args) {

        //类加载机制
        ClassLoaderClass clc = new ClassLoaderClass();
//        clc.classLoader();
//        clc.customFileClassLoaderTest();
//        clc.customDecryptClassLoaderTest();
        clc.customContextClassLoaderTest();
    }
}
