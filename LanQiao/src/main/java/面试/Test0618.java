package 面试;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test0618 {
    //多个生产者，生产数据，满则阻塞
//缓冲区，有界
//多个消费者，消费数据，空则阻塞



    static ReentrantLock lock1=new ReentrantLock();
    static int i=0;

    static int size=10;

    static Deque<Integer> deque=new LinkedList<>();
    static Condition condition1 = lock1.newCondition();
    static Condition condition2 = lock1.newCondition();
    public static void main(String[] args) {


        Thread s1 = new Thread(() -> {
            while (true){
                lock1.lock();

                try {
                    if (deque.size()==size){
                        System.out.println("缓冲满");
                        //condition2.signal();
                        condition1.await();
                    }else {
                        System.out.println("生产者[1]生产["+i+"]");
                        deque.addLast(i);
                        i++;
                        condition2.signal();
                    }
                }catch (Exception e){

                }finally {
                    lock1.unlock();
                }
            }
        });


        Thread s2 = new Thread(() -> {
            while (true){
                lock1.lock();

                try {
                    if (deque.size()==size){
                        System.out.println("缓冲满");
                        //condition2.signal();
                        condition1.await();
                    }else {
                        System.out.println("生产者[2]生产["+i+"]");
                        deque.addLast(i);
                        i++;
                        condition2.signal();
                    }
                }catch (Exception e){

                }finally {
                    lock1.unlock();
                }
            }
        });


        Thread x1 = new Thread(() -> {
            while (true){
                lock1.lock();

                try {
                    if (deque.isEmpty()||deque.size()==0){
                        System.out.println("缓冲空");
                        condition1.signal();
                        condition2.await();
                    }else {
                        Integer t = deque.pollFirst();
                        System.out.println("消费者[1] 消费["+t+"]");
                        condition1.signal();
                    }
                }catch (Exception e){

                }finally {
                    lock1.unlock();
                }
            }
        });


        Thread x2 = new Thread(() -> {
            while (true){
                lock1.lock();

                try {
                    if (deque.isEmpty()||deque.size()==0){
                        System.out.println("缓冲空");
                        condition1.signal();
                        condition2.await();
                    }else {
                        Integer t = deque.pollFirst();
                        System.out.println("消费者[2] 消费["+t+"]");
                        condition1.signal();
                    }
                }catch (Exception e){

                }finally {
                    lock1.unlock();
                }
            }
        });

        s1.start();
        x1.start();
        s2.start();
        x2.start();


    }
}
