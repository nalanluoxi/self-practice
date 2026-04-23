package 多线程;

import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：卖电影票
 * @Date：2025/4/27 21:57
 * @Filename：卖电影票
 */
public class 卖电影票 {

    static int allcount;
    static int status;
    static ReentrantLock lock = new ReentrantLock();
    static Object lock1 = new Object();

    public static void main(String[] args) throws InterruptedException {
        allcount = 1000;
        Thread thread1 = new Thread(() -> {
            while (allcount > 0) {
                lock.lock();
                try {
                    allcount--;
                    System.out.println("用户1买一张票，还剩" + allcount + "张票");
                    sleep(1000*3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            while (allcount > 0) {
                lock.lock();
                try {
                    allcount--;
                    System.out.println("用户2买一张票，还剩" + allcount + "张票");
                    sleep(1000*3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("票卖完了");
        System.out.println("allcount=" + allcount);
        /*Thread thread1=new Thread(()->{
            while (allcount>0){
                synchronized (lock1){
                    if (allcount>0){
                        allcount--;
                        System.out.println("用户1买一张票，还剩"+allcount+"张票");
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (allcount == 0) {
                        break;
                    }
                }
            }
        });

        Thread thread2=new Thread(()->{
            while (allcount>0){
                synchronized (lock1){
                    if (allcount>0){
                        allcount--;
                        System.out.println("用户2买一张票，还剩"+allcount+"张票");
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (allcount == 0) {
                        break;
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("票卖完了");
        System.out.println("allcount="+allcount);*/


    }
}
