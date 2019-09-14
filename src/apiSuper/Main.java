package apiSuper;

import java.lang.String;

public class Main {
    public static void main(String[] args) {
        //Collection 集合接口类
//        CollectionClass cc = new CollectionClass();
//        cc.superCollection();
//        cc.collectionUtils();

        //Generics 泛型接口类
//        GenericsClass gc1 = new GenericsClass();   // 等同于   GenericsClass<Object> gc1 = new GenericsClass<>();
//        GenericsClass<String> gc2 = new GenericsClass<>();
//        gc1.setVariable("I am a Object Type");
//        gc2.setVariable("I am a String Type");
//        GenericsClass.getGenericsMethod(new String[]{"f","u","c","k"});
//        gc1.genericsWildcard();

        //Map 类
//        MapClass mapClass = new MapClass();
//        mapClass.MapOperation();

        //Exception 类
//        ExceptionClass.exceptionOperation();

        //Thread 类
//        ThreadClass thread = new ThreadClass();
//        thread.createThread();
//        thread.sleepThread();
//        thread.anonymousThread();
//        thread.threadSecureProblem();
//        thread.threadSecureSyncCodeBlock();
//        thread.threadSecureSyncLock();
//        thread.threadWaitNotifyMechanism();
//        thread.threadCreatePool(5);
//        thread.threadPool(5);

        //Socket类
//        SocketClass socketClass = new SocketClass();
//        socketClass.HelloSocket();
//        socketClass.uploadFileSocket();
//        socketClass.downloadFileSocket();
//        socketClass.BrowserServiceSocket();

        //Stream类
//        StreamClass streamClass = new StreamClass();
//        streamClass.StreamFiltter();
//        streamClass.ObtainStream();
//        streamClass.StreamOperation();

        //Annotation类
//        AnnotationClass anno = new AnnotationClass();
//        anno.deprecateMethod();

        //Reflection类
//        ReflectionClass reflect = new ReflectionClass();
//        reflect.getClassObject();
//        reflect.getClassInfo();
//        reflect.classReflectOperate();
//        reflect.getReflectGenerics();

        //动态编译方式
        DynamicCompileClass dynamicCompile = new DynamicCompileClass();
        dynamicCompile.RuntimeExecute();
//        dynamicCompile.JavaCompilerExecute();

        //动态脚本引擎
//        ScriptEngineClass sec = new ScriptEngineClass();
//        sec.JsScriptEngine();
    }
}
