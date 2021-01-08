package juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

/**
 * 异步回调
 * @see java.util.concurrent.Future
 */
public class FutureDemo {

    //如果无需返回值,可以调用CompletableFuture.runAsync方法,泛型记得给Void
    public void test1() {
        //此方法执行后,主线程就不管它了
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("runAsync ...");
            }
        })
        //thenRunAsync会在run()任务执行完毕之后进行,它不关心执行结果,区别于thenRun它是异步的,并且会阻塞此生命周期后的非异步动作
        .thenRunAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thenRunAsync ...");
            }
        })
        //thenRun会在run()任务执行完毕之后进行,它不关心执行结果
        .thenRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("thenRun ...");
            }
        })
        //handleAsync会在整个任务流程结束之后进行,区别于handle它是异步的,并且会阻塞此生命周期后的非异步动作
        .handleAsync(new BiFunction<Void, Throwable, Void>() {
            @Override
            public Void apply(Void aVoid, Throwable throwable) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("handleAsync ...");
                return null;
            }
        })
        //handle会在整个任务流程结束之后进行
        .handle(new BiFunction<Void, Throwable, Void>() {
            @Override
            public Void apply(Void aVoid, Throwable throwable) {
                System.out.println("handle ...");
                return null;
            }
        })
        //如果此异步任务执行失败会来到这里
        .exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                System.out.println("error: " + throwable.getMessage());
                return null;
            }
        });

        System.out.println("MainRun ...");

        //Main线程会阻塞在这里
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //如果需要返回值,可以调用CompletableFuture.runAsync方法
    public int test2() {
        //此方法执行后,主线程就不管它了
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int rst = (int) (Math.random() * 100);
                if (rst < 50) return rst/0;
                return rst;
            }
        })
        //whenCompleteAsync会在run()任务成功执行完毕之后进行,然后就可以在此获取执行结果了,区别whenComplete它会阻塞此生命周期后的非异步动作
        .whenCompleteAsync(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("whenCompleteAsync ... Number is " + integer);
            }
        })
        //whenComplete会在run()任务成功执行完毕之后进行,然后就可以在此获取执行结果了
        .whenComplete(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("whenComplete ... Number is " + integer);
            }
        })
        //如果此异步任务执行失败会来到这里,并且可以有返回值
        .exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) {
                System.out.println("error: " + throwable.getMessage());
                return 500;
            }
        });

        System.out.println("MainRun ...");

        //Main线程会阻塞在这里
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
