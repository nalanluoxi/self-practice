package likou;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test0319 {


    static ReentrantLock lock = new ReentrantLock();

    static int i = 1;
    static int now = 1;

    public static void main(String[] args) {
        // 20 shengcahn xiaofei

        Condition a1 = lock.newCondition();
        Condition a2 = lock.newCondition();

        Queue<Integer> queue = new LinkedList<>();


        Thread t1 = new Thread((() -> {
            while (true) {
                lock.lock();
                try {
                    if (now > 20) {
                        a2.signal();
                        break;
                    }
                    if (!queue.isEmpty()){
                        a2.signal();
                    }
                    if (queue.size() == 10) {
                        try {
                            a1.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("生产者：" + now);
                    queue.add(now);
                    now++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }finally {
                    lock.unlock();
                }


            }
        }));

        Thread t2 = new Thread((() -> {
            while (true) {
                lock.lock();
                try {

                    if (now > 20 && queue.isEmpty()) {
                        break;
                    }
                    if (queue.size()<10){
                        a1.signal();
                    }
                    if (queue.isEmpty()) {
                        try {
                            a2.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    Integer poll = queue.poll();
                    System.out.println("消费者" + poll);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }finally {
                    lock.unlock();
                }

            }
        }));

        t1.start();
        t2.start();

        /*Thread t1 = new Thread(() -> {
            while (true){
                lock.lock();
                try {
                    if (i!=1){
                        a1.await();
                    }else {

                        if (now>20){
                            break;
                        }
                        System.out.println("生产者"+now);
                        queue.add(now);
                        now++;
                        i=2;
                        a2.signal();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });



        Thread t2 = new Thread(() -> {
            while (true){
                lock.lock();
                try {
                    if (i!=2){
                        a2.await();
                    }else {
                        if (now>20){
                            break;
                        }
                        Integer poll = queue.poll();
                        System.out.println("消费者"+poll);
                        i=1;
                        a1.signal();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });


        t1.start();
        t2.start();
*/
    }
}
