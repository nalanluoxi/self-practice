package example.java2;

import likou.最低票价;

import java.util.Deque;
import java.util.LinkedList;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test0227 {

    static ReentrantLock lock = new ReentrantLock();

    static int status=1;
    static  int i=1;

    public static void main(String[] args) {
        Condition a1 = lock.newCondition();
        Condition a2 = lock.newCondition();

        Deque<Integer> deque=new LinkedList<>();

        Thread t1=new Thread(()->{
            while (i<=10){
                lock.lock();
                try {
                    if (status==1){
                        deque.addLast(i++);
                        status=2;
                        a2.signal();
                    }else {
                        a1.await();
                    }
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }
        });


        Thread t2=new Thread(()->{
            while (i<=10){
                lock.lock();
                try {
                    if (status==2){
                        Integer last = deque.pollLast();
                        deque.addLast(last+1);
                        status=1;
                        a1.signal();
                    }else {
                        a2.await();
                    }
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }

        });


        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }catch (Exception e){

        }

        while (!deque.isEmpty()){
            System.out.println(deque.pollFirst());
        }

    }


}
