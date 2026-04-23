package example.java2;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Test0128 {

   static int nums=1;
   static int status=1;

    public static void main(String[] args) {
        ReentrantLock lock=new ReentrantLock();
        Condition a1 = lock.newCondition();
        Condition a2 = lock.newCondition();


        Thread t1 = new Thread(() -> {
            while (nums != 100) {
                lock.lock();
                try {
                    while (status == 1) {
                        a1.await();
                    }
                    System.out.println(nums++);
                    status = 2;
                    a2.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });


        Thread t2 = new Thread(() -> {
            while (nums != 100) {
                lock.lock();
                try {
                    while (status == 2) {
                        a2.await();
                    }
                    System.out.println(nums++);
                    status = 1;
                    a1.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });


        t1.start();
        t2.start();

    }
}
