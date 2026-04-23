package example.java2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test0303 {


    static int status=1;
    static int i=1;
    public static void main(String[] args) {


        ReentrantLock lock=new ReentrantLock();
        Condition a1 = lock.newCondition();
        Condition a2 = lock.newCondition();
        Condition a3 = lock.newCondition();

        Thread t1=new Thread(()->{
            while (true){
                lock.lock();
                try {
                    if (i>10){
                        break;
                    }
                    if (status!=1 && i<=10){
                        a1.await();
                    }else {
                        System.out.println("线程1："+i++);
                        status=2;
                        a2.signal();
                    }
                }catch (Exception e){

                } finally {

                        lock.unlock();

                }
            }
        });

        Thread t2=new Thread(()->{
            while (true){
                lock.lock();
                try {
                    if (i>10){
                        a2.signal();
                        a3.signal();
                        break;
                    }
                    if (status!=2 && i<=10){
                        a2.await();
                    }else {
                        System.out.println("线程2:"+i++);
                        status=3;
                        a3.signal();
                    }
                }catch (Exception e){

                } finally {

                    lock.unlock();

                }
            }
        });

        Thread t3=new Thread(()->{
            while (true){
                lock.lock();
                try {
                    if (i>10){
                        break;
                    }
                    if (status!=3 && i<=10){
                        a3.await();
                    }else {
                        System.out.println("线程3:"+i++);
                        status=1;
                        a1.signal();
                    }
                }catch (Exception e){

                } finally {

                    lock.unlock();

                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        }catch (Exception e){

        }
        System.out.println("结束");



    }
}
