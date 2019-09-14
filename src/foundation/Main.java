package foundation;

import java.lang.String;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        System.out.println("The result is :"+ sum(0,1,2));
//        createArray();
//        arrayReserve();

//        Person person = new Person();
//        person.setInfo("蔡宏德",'男',20);
//        person.show();

        //方法重载
//        Person person2 = new Person('女',18,"刘亦菲");
//        person2.show();

        //可变参数
        //VariableArgs.argsInput("可变参数终极写法：",new Integer(100), "string",'a',8.888,100L,200F);


        //斗地主案例
//        Doudizhu ddz = new Doudizhu();
//        ddz.playGame();

        //lambda函数式编程
        //1.有参数有返回值形式
        //不简化写法
        /*invokeLambda(0,"xixixi", (a,b) -> {
            System.out.println("满足条件我才会执行："+ b);
            return  a;
        });*/
        //超级简化写法
        //invokeLambda(10,"xixixi", (a,b) -> a );
        //2.有参数无返回值形式
        //invokeLambda(10,20, (a,b) -> System.out.println("fdsjflsdjf") );



        //方法引用进一步简化lambda
        MethodReferenceClass mrc = new MethodReferenceClass();
        mrc.operateMethodReference();












        //研究Java垃圾回收finalize()机制
//        finalizeShow(args);
        //DeathCondition();
    }

    private static void DeathCondition() {
        BookClass novel = new BookClass(true);
        // Proper cleanup:
        novel.checkIn();
        // Drop the reference, forget to clean up:
        new BookClass(true);
        // Force garbage collection & finalization:
        System.gc();
    }

    private static void finalizeShow(String[] args) {
        // As long as the flag hasn't been set,make FinalizeClass and Strings:
        while(!FinalizeClass.f) {
            new FinalizeClass();
            new String("To take up space");
        }
        System.out.println(
                "After all FinalizeClass have been created:\n" +
                        "total created = " + FinalizeClass.created +
                        ", total finalized = " + FinalizeClass.finalized);
        // Optional arguments force garbage
        // collection & finalization:
        if(args.length > 0) {
            if(args[0].equals("gc") || args[0].equals("all")) {
                System.out.println("gc():");
                System.gc();
            }
            if(args[0].equals("finalize") || args[0].equals("all")) {
                System.out.println("runFinalization():");
                System.runFinalization();
            }
        }
        System.out.println("bye!");
    }

    private static void invokeLambda(int a ,String b, lambdaClass lambda){
        //lambda还有延迟执行的特性,可以避免不必要的性能浪费！
        if (a == 0 ) lambda.calculator(a,b);
    }

}
