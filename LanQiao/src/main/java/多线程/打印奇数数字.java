package 多线程;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：打印奇数数字
 * @Date：2025/4/28 11:37
 * @Filename：打印奇数数字
 */
public class 打印奇数数字 {
    public static void main(String[] args) throws InterruptedException {
        MyThread thread1=new MyThread();
        MyThread thread2=new MyThread();
        MyThread thread3=new MyThread();
        MyThread thread4=new MyThread();
        thread1.setName("小明");
        thread2.setName("小红");
        thread3.setName("小蓝");
        thread4.setName("小绿");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        System.out.println("打印完毕");
    }

    public static class MyThread extends Thread{
        static int i = 0;
        int target=10;
        @Override
        public void run() {
            while (i<=target){
                synchronized (MyThread.class){
                    if (i>target){
                        return;
                    }
                    if (i%2!=0){
                        System.out.println("线程:"+Thread.currentThread().getName()+"打印了一个奇数数字"+i);
                    }
                    i++;
                }
            }
        }
    }
}
