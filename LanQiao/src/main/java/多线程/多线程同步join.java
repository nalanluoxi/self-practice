package 多线程;



import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;


/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：多线程同步join
 * @Date：2025/4/19 9:57
 * @Filename：多线程同步join
 */
@Slf4j
public class 多线程同步join {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            text2();
        }

        //text();
    }

    public static void text2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                sleep(1);
                log.debug("t1执行结束"+System.currentTimeMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                sleep(2);
                log.debug("t2执行结束"+System.currentTimeMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.debug(Thread.currentThread().getName()+":开始");
        t1.start();
        t2.start();
        long start = System.currentTimeMillis();
        t2.join();t1.join();
        long end = System.currentTimeMillis();
        log.debug(Thread.currentThread().getName()+":结束");
        log.debug("耗时："+(end-start));
    }

    static int num=0;
    public static void text() throws InterruptedException {
        log.debug(Thread.currentThread().getName()+":开始");

        Thread thread = new Thread(() -> {
            log.debug(Thread.currentThread().getName() + ":开始");
            try {
                log.debug(Thread.currentThread().getName() + ":休眠");
                sleep(1000);
                log.debug(Thread.currentThread().getName() + ":休眠结束");
                num = 20;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug(Thread.currentThread().getName() + ":结束");
        });


        thread.start();
        thread.join();
        System.out.println(num);
        log.debug(Thread.currentThread().getName()+":结束");

    }
}

