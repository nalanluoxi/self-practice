package example.java2.MyThreadPoolTest;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyThreadPoolTest
 * @Project：LanQiaoBei
 * @name：BlockingQueue
 * @Date：2025/4/22 17:13
 * @Filename：BlockingQueue
 */
public class MyBlockingQueue <T extends Runnable>{
    private Queue<T> queue = new ArrayDeque<>();
    private Lock lock = new ReentrantLock();
    private Condition emptyCondition = lock.newCondition();
    private Condition fullCondition = lock.newCondition();
    private int cap;

    public int getsize(){
        return queue.size();
    }

    public MyBlockingQueue(int cap) {
        this.cap = cap;
    }

    public boolean offer (T task)  {
        try {
            lock.lock();
            while (queue.size() == cap) {
                return false;
            }
            queue.offer(task);
            emptyCondition.signal();
            System.out.println("成功添加到等待队列 当前大小"+queue.size());
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean put(T task){
        try {
            lock.lock();
            while (queue.size()==cap){
                fullCondition.await();
            }
            queue.offer(task);
            emptyCondition.signal();
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public T take(){
        try {
            lock.lock();
            while (queue.size()==0){
                emptyCondition.await();
            }
            T poll = queue.poll();
            return poll;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public T get(){
        try {
            lock.lock();
            T poll = queue.poll();
            return poll;
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(T runnable) {
        lock.lock();
        try {
            return queue.remove(runnable);
        }finally {
            lock.unlock();
        }
    }
}
