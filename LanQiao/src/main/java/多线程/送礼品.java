package 多线程;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：送礼品
 * @Date：2025/4/28 11:30
 * @Filename：送礼品
 */
public class 送礼品 {
    static int count;
    static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {
        MyThread thread1=new MyThread();
        MyThread thread2=new MyThread();
        MyThread thread3=new MyThread();
        MyThread thread4=new MyThread();
        thread1.setName("小明");
        thread2.setName("小红");
        thread3.setName("小刚");
        thread4.setName("小蓝");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        List<Integer> list = MyThread.list;
        Collections.sort(list);

        System.out.println("礼物已经送完了");
        System.out.println(list);
    }

    public static class MyThread extends Thread{
        static volatile int count=50;

        static List<Integer> list=new LinkedList<>();
        Object lock=new Object();
        @Override
        public void run() {
            while (count>10){
                synchronized (lock){
                    if (count>10){
                        count--;
                        int now = count;
                        System.out.println("用户:"+Thread.currentThread().getName()+"送了一个礼物，现在还剩余"+now+"个礼物"+"个礼物");
                        list.add(now);
                    }
                }
            }
        }
    }
}
