package foundation;

public class FinalizeClass {
    static boolean gcrun = false;
    static boolean f = false;
    static int created = 0;
    static int finalized = 0;
    int i;

    //constructor
    FinalizeClass(){
        i = ++created;
        if (created == 47)
            System.out.println("Created 47");
    }

    //垃圾收集并不等于“破坏”!
    public void finalize(){
        if(!gcrun) {
        // The first time finalize() is called:
            gcrun = true;
            System.out.println(
                    "Beginning to finalize after " +
                            created + " FinalizeClass have been created");
        //如果垃圾收集器嗅觉到不对劲开始工作时，停止创建对象
            System.out.println(
                    "Finalizing FinalizeClass #47, " +
                            "Setting flag to stop FinalizeClass creation");
            f = true;

        }
        if(i == 47) {
            System.out.println(
                    "Finalizing FinalizeClass #47, " +
                            "Setting flag to stop FinalizeClass creation");
            f = true;
        }
        finalized++;
        if(finalized >= created)
            System.out.println(
                    "All " + finalized + " finalized");
    }
}
