package example.java2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2
 * @Project：LanQiaoBei
 * @name：example
 * @Date：2025/3/20 11:18
 * @Filename：example
 */
public class example {



    static ReentrantLock lock = new ReentrantLock();
    static Condition a2 = lock.newCondition();
    static Condition a3 = lock.newCondition();
    static int state=0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int i = scanner.nextInt();
        int j = scanner.nextInt();
        int k = scanner.nextInt();

        Thread a = new Thread(()->{
            lock.lock();
            try {
                IFo iFo = new FO();
                iFo.start(i);
                state=2;

                sleep(1000);
                a2.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });
        Thread b = new Thread(()->{
            lock.lock();
            try {
                while (state!=2){
                    a2.await();
                }
                IFo iFo = new FO();
                iFo.start(j);
                state=3;
                a3.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });
        Thread c = new Thread(()->{
            lock.lock();
            try {
                while (state!=3){
                    a3.await();
                }
                IFo iFo = new FO();
                iFo.start(k);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });
        a.start();
        b.start();
        c.start();
    }
}
