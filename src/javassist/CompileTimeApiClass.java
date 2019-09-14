package javassist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

/**
 * javassist 是一个开源的分析/编译/创建java字节码的类库
 */
public class CompileTimeApiClass {


    //运行期创建新的类并编译成字节码
    public void CtClassMakeClass() throws Exception {
        //通过类池创建一个创建类的句柄
        ClassPool pool = ClassPool.getDefault();
        CtClass ncc = pool.makeClass("javassist.HelloJavassist");

        //创建属性
        CtField fieldA = CtField.make("private String ctname;",ncc);
        CtField fieldB = CtField.make("private int ctage;",ncc);
        ncc.addField(fieldA);
        ncc.addField(fieldB,"0");

        //创建构造器
        // public HelloJavassist(String name,int age){}
        CtConstructor constructorA = new CtConstructor(new CtClass[]{pool.get("java.String"),CtClass.intType},ncc);
        constructorA.setBody("{ $0.ctname = $1;$0.ctage = $2; }");    //$0代表this,$1代表第一个参数，$2代表第二个参数
        // public HelloJavassist(){}
        CtConstructor constructorB = new CtConstructor(new CtClass[]{},ncc);
        //即使是无参构造器也必须要setBody()
        // 否则加载类会抛出异常java.java.lang.ClassFormatError: Absent Code attribute in method that is not native or abstract in class file
        constructorB.setBody("{}");
        ncc.addConstructor(constructorA);
        ncc.addConstructor(constructorB);

        //创建方法
        CtMethod methodA = CtMethod.make("public String getCtname() { return ctname; }",ncc);
        CtMethod methodB = CtMethod.make("public void setCtname(String ctname) { $0.ctname = $1; }",ncc);
        CtMethod methodC = new CtMethod(CtClass.intType,"getCtage",new CtClass[]{},ncc);
        methodC.setBody("{ return ctage; }");
        CtMethod methodD = new CtMethod(CtClass.voidType,"setCtage",new CtClass[]{CtClass.intType},ncc);
        methodD.setBody("{ $0.ctage = $1; }");
        ncc.addMethod(methodA);
        ncc.addMethod(methodB);
        ncc.addMethod(methodC);
        ncc.addMethod(methodD);

        //生成字节码
        ncc.writeFile("/home/cahoder/IdeaProjects/java8/src");    //创建写的时候这个类是冻结的状态必须解冻，才可以被其他人调用
        ncc.defrost();
    }

    //运行期使用类
    public void CtClassUseClass() throws Exception {
        //通过类池创建一个创建类的句柄
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("javassist.HelloJavassist");

        //在原有的类基础上添加新的方法
        CtMethod newM = new CtMethod(CtClass.voidType,"sayHello",new CtClass[]{pool.get("java.util.List")},cc);
        newM.setModifiers(Modifier.PRIVATE);
        newM.setBody("{ for (int i = 0; i < $1.size(); i++) {System.out.print($1.get(i));};}");
        cc.addMethod(newM);
        //在原有方法的代码上追加代码
        newM.insertBefore("System.out.print(\"Hello! My name is : \");");
        newM.insertAfter("System.out.print(\" Nice to meet you !\");");

        //使用反射调用类
        Class<?> HelloJavassist = cc.toClass();
        Constructor<?> constructor = HelloJavassist.getDeclaredConstructor(String.class, int.class);
        Object instance = constructor.newInstance("彩虹的",15);
        Method sayHello = HelloJavassist.getDeclaredMethod("sayHello",List.class);
        ArrayList<String> lists = new ArrayList<>();
        lists.add("C");
        lists.add(".");
        lists.add("H");
        lists.add("D");
        lists.add("E");
        sayHello.setAccessible(true);   //私有方法关闭需要先关闭安全检测
        sayHello.invoke(instance,lists);
    }
}
