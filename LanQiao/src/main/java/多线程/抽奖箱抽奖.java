package 多线程;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：抽奖箱抽奖
 * @Date：2025/4/28 15:25
 * @Filename：抽奖箱抽奖
 */
public class 抽奖箱抽奖 {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer>list=new ArrayList<>();
        Collections.addAll(list,2,5,10,20,50,100,200,500,800,80,300,700);
        System.out.println(list.size());
        MyThread thread1=new MyThread(list);
        MyThread thread2=new MyThread(list);
        MyThread thread3=new MyThread(list);
        MyThread thread4=new MyThread(list);
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
        System.out.println("抽奖完毕");
        System.out.println("一共抽了"+MyThread.count+"个礼物");


    }

    public static class MyThread extends Thread {
        ArrayList list;
        static int count;

        public MyThread(ArrayList list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (list.size() > 0) {
                synchronized (MyThread.class) {
                    if (list.size()<=0){
                        return;
                    }
                    Collections.shuffle(list);
                    Object remove = list.remove(0);
                    count++;
                    System.out.println("用户:" + Thread.currentThread().getName() + "抽了一个" + remove);
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


}
