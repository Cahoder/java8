package foundation;

public class MyThread extends Thread{

    private Runnable task = null;

    public void setTask(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return this.task;
    }

    @Override
    public void run() {
        while (true){
            //使线程一直处于RUNNABLE状态
            synchronized (super.getClass()){
                if (this.task != null){
                    this.task.run();
                    this.task = null;
                }
            }
        }
    }
}
