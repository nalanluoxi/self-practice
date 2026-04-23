package example.java2.MyThreadPoolTest;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyThreadPollTest
 * @Project：LanQiaoBei
 * @name：Test
 * @Date：2025/4/21 16:31
 * @Filename：Test
 */
public class Test {
     static volatile int nums=0;
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        MyThreadPool threadPool = new MyThreadPool(
                2,
                4,
                60, TimeUnit.SECONDS,
                new MyBlockingQueue<>(3),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r);
                    }
                },
                MyThreadPool.CallerRuns.class.newInstance()
        );


        for (int i = 0; i < 6; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("==========================="+threadName+"===========================");
                    int i1 = nums++;
                    System.out.println(threadName + " 开始执行任务"+ i1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(threadName + " 执行结束任务："+ i1);
                }
            });
        }
        threadPool.shutdown();

    }
}
