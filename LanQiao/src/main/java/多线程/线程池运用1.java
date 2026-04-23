package 多线程;

import example.java2.MyThreadPoolTest.MyBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：线程池运用1
 * @Date：2025/5/15 21:17
 * @Filename：线程池运用1
 */
public class 线程池运用1 {
    public static void main(String[] args) {
        /*ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactory(() -> {
                    return
                }),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        List<CompletableFuture> list = new ArrayList<>();*/
        

    }

    public static boolean check(){
        System.out.println("执行任务");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
