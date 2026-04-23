package example.java2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：多线程交替打印技术偶数
 * @Date：2025/4/18 18:15
 * @Filename：多线程交替打印技术偶数
 */
public class 多线程交替打印技术偶数 {
    static ReentrantLock lock = new ReentrantLock();

    static Condition a1 = lock.newCondition();
    static Condition a2 = lock.newCondition();
    static int num = 1;
    static int state = 1;

    public static void main(String[] args) {
        Thread demo1 = new Thread(() -> {
            while (num != 100) {
                lock.lock();
                try {
                    while (state != 1) {
                        a1.await();
                    }
                    System.out.println(num++);
                    state = 2;
                    a2.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread demo2 = new Thread(() -> {
            while (num != 101) {
                lock.lock();
                try {
                    while (state != 2) {
                        a2.await();
                    }
                    System.out.println(num++);
                    state = 1;
                    a1.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });

        demo1.start();
        demo2.start();

    }
}
