package 多线程;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：抢红包
 * @Date：2025/4/28 11:53
 * @Filename：抢红包
 */
public class 抢红包 {
    public static void main(String[] args) throws InterruptedException {
        MyThread thread1=new MyThread();
        MyThread thread2=new MyThread();
        MyThread thread3=new MyThread();
        MyThread thread4=new MyThread();
        MyThread thread5=new MyThread();
        thread1.setName("小明");
        thread2.setName("小红");
        thread3.setName("小刚");
        thread4.setName("小蓝");
        thread5.setName("小绿");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        System.out.println("抢红包完毕");

    }

    public static class MyThread extends Thread{
        static ReentrantLock lock = new ReentrantLock();

        static double all=100.0;
        static int count=3;

        @Override
        public void run() {

                lock.lock();
                try {
                    if (count>0){
                        if (count==1){
                            double now = all;
                            now=Math.round(now*100)/100.0;
                            all=0;
                            count--;
                            System.out.println("用户:"+Thread.currentThread().getName()+"抢到了"+now+"元");
                            return;
                        }
                        double money = getMoney();
                        System.out.println("用户:"+Thread.currentThread().getName()+"抢到了"+money+"元");
                    }else {
                        System.out.println("用户:"+Thread.currentThread().getName()+"没抢到红包");
                    }
                }finally {
                    lock.unlock();
                }

        }

        private double getMoney(){
            Random random=new Random();
            double v = all - (count - 1) * 0.01;
            double money=random.nextDouble(v);
            money=Math.round(money*100)/100.0;
            if (money<0.01){
                money=0.01;
            }
            all-=money;
            count--;
            return money;
        }


    }
}
