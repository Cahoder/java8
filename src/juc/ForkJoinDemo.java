package juc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 分支合并思想: 将任务拆分为子任务去执行,子任务执行完毕在返回给父合并
 * 分支合并特点: 任务窃取 -- 因为每个任务都维护着一个双端队列,因此自己的做完了可以去偷别人的任务做
 * @see java.util.concurrent.ForkJoinPool  //它来提供线程池支持ForkJoinTask
 * @see java.util.concurrent.ForkJoinTask  //负责指定任务和任务拆分合并
 *      fork() 方法 -- 从异步执行的任务启动一个新的子任务
 *      join() 方法 -- 让异步执行的任务等待兄弟异步任务的完成
 *      --它的子类
 *      @see java.util.concurrent.RecursiveAction   -- 适用于执行的任务不需要返回结果
 *      @see java.util.concurrent.RecursiveTask   -- 适用于执行的任务需要返回结果
 *      @see java.util.concurrent.CountedCompleter --
 */
public class ForkJoinDemo {

    //使用分支合并思想计算斐波那契数的结果
    private static class Fibonacci extends RecursiveTask<Integer> {
        private static final long serialVersionUID = 1047572343562182231L;
        final int n;
        Fibonacci(int n) { this.n = n; }

        @Override
        protected Integer compute() {
            if (n <= 1) return n;
            Fibonacci f1 = new Fibonacci(n - 1);
            //拆分成子任务
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            //最后又合并结果
            return f2.compute() + f1.join();
        }
    }

    //使用分支合并思想打印
    private static class Print extends RecursiveAction {
        /**
         *  每个小任务最多只打印20个数
         */
        private static final int MAX = 20;
        private static final long serialVersionUID = 8807558846387094471L;
        private final int start;
        private final int end;

        public Print(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if ((end - start) < MAX) {
                for (int i = start; i < end; i++) {
                    System.out.println(Thread.currentThread().getName()+"i的值为"+i);
                }
            }
            else {
                int middle = (start+end)>>1;
                Print left = new Print(start, middle);
                left.fork();
                Print right = new Print(middle, end);
                right.fork();
            }
        }

    }
    public int test_forkjoin_1(int Fibonacci_order){
        //创建一个线程池来跑forkjoin任务
        ForkJoinPool pool = new ForkJoinPool();
        try {
            Fibonacci task = new Fibonacci(Fibonacci_order);
            //计时开始
            Instant start = Instant.now();

            //execute方法不会有放回值
            /*pool.execute(task);*/

            // submit方法需要先获取任务,再获取结果
            /*ForkJoinTask<Integer> submit = pool.submit(task);
            try {
                return submit.get();
            } catch (InterruptedException | ExecutionException ignored) {}*/

            //invoke方法可以直接拿到结果
            Integer fibonacci = pool.invoke(task);

            //计时结束
            Instant end = Instant.now();
            System.out.println("forkjoin计算耗费时间(毫秒): "+ Duration.between( start,end ).toMillis());

            return fibonacci;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
        return -1;
    }

    //JDK1.8提供了并发流计算,充分利用CPU资源
    public int test_stream_parallel(int Fibonacci_order){
        //计时开始
        Instant start = Instant.now();

        AtomicInteger ret = new AtomicInteger();
        Stream.iterate(new int[]{0, 1}, i -> new int[]{i[1], (i[0] + i[1])})
                .parallel()
                .limit(Fibonacci_order)
                .skip(Fibonacci_order-1)
                .forEach(ints -> ret.set(ints[1]));

        //计时结束
        Instant end = Instant.now();
        System.out.println("并发流计算耗费时间(毫秒): "+ Duration.between( start,end ).toMillis());
        return ret.get();
    }

    public void test_forkjoin_2(int print_nums) {
        //创建一个线程池来跑forkjoin任务
        ForkJoinPool pool = new ForkJoinPool();
        Print task = new Print(0,print_nums);

        try {
            pool.submit(task);
            pool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}
