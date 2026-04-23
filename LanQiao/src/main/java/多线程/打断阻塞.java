package 多线程;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：打断阻塞
 * @Date：2025/4/19 10:17
 * @Filename：打断阻塞
 */
@Slf4j
public class 打断阻塞 {
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    static int num=0;
    public static void test1() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            log.debug("线程:" + Thread.currentThread() + "开始");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("结束");
          /*  while (!Thread.currentThread().isInterrupted()){
                System.out.println(num++);
            }*/
        });

        t1.start();
        Thread.sleep(1000);
        System.out.println("打断");
        t1.interrupt();
        System.out.println("t1打断标志:"+t1.isInterrupted());
        System.out.println("t1打断标志:"+t1.isInterrupted());



    }
}
