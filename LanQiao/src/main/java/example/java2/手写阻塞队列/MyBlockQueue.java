package example.java2.手写阻塞队列;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.手写阻塞队列
 * @Project：LanQiaoBei
 * @name：MyBlockQueue
 * @Date：2025/6/8 21:46
 * @Filename：MyBlockQueue
 */
public class MyBlockQueue<T> {
    private int capity;
    private int size;

    private List<T> list;

    public ReentrantLock reentrantLock = new ReentrantLock();
    Condition a2 = reentrantLock.newCondition();
    Condition a1 = reentrantLock.newCondition();

    public MyBlockQueue(int capity) {
        if (capity <= 0) {
            throw new IllegalArgumentException("capity must be >0");
        }
        this.capity = capity;
        this.size = 0;
        list = new LinkedList<>();
    }

    public synchronized void put(T value) {
        reentrantLock.lock();
        try {
            while (size == capity) {
                a1.await();
            }
            list.add(value);
            size++;
            a2.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            reentrantLock.unlock();
        }

    }

    public T poll() {
        reentrantLock.lock();
        try {
            while (size == 0) {
                a2.await();
            }
            T remove = list.remove(0);
            size--;
            a1.signal();
            return remove;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            reentrantLock.unlock();
        }
    }


}
