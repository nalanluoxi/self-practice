package example.java2.MyThreadPoolTest;

import java.util.HashSet;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyThreadPollTest
 * @Project：LanQiaoBei
 * @name：MyThreadPoll
 * @Date：2025/4/21 16:31
 * @Filename：MyThreadPoll
 */
public class MyThreadPool {
    private volatile int corePoolSize;
    private volatile int maxPoolSize;
    private volatile long keepAliveTime;
    private volatile TimeUnit timeUnit;

    private volatile int largestPoolSize;

    private ReentrantLock mainLock;

    private final AtomicInteger size;
    /*private volatile BlockingQueue<Runnable> workQueue;*/

    private volatile MyBlockingQueue<Runnable> workQueue;

    private final HashSet<Worker> workers;

    private volatile ThreadFactory threadFactory;

    private volatile RejectedHandler rejectedHandler;

    private volatile String status;

    private static final String RUNNING = "RUNNING";
    private static final String SHUTDOWN = "SHUTDOWN";
    private static final String STOP = "STOP";

    public MyThreadPool(int corePoolSize,
                        int maxPoolSize,
                        long keepAliveTime, TimeUnit timeUnit,
                        MyBlockingQueue<Runnable> workQueue,
                        ThreadFactory threadFactory,
                        RejectedHandler rejectedHandler) {
        this.workers = new HashSet();
        this.corePoolSize = corePoolSize == 0 ? 1 : corePoolSize;
        this.maxPoolSize = maxPoolSize == 0 ? 1 : maxPoolSize;
        this.keepAliveTime = keepAliveTime <= 0 ? 0 : keepAliveTime;
        this.timeUnit = timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
        this.size = new AtomicInteger(0);
        this.workQueue = workQueue != null ? workQueue : new MyBlockingQueue<>(10);
        this.threadFactory = threadFactory;
        this.rejectedHandler = rejectedHandler != null ? rejectedHandler : new DisRuns();
        this.status = RUNNING;
        this.mainLock = new ReentrantLock();
        this.largestPoolSize = 0;
    }


    public void execute(Runnable runnable) {
        if (runnable != null) {
            int nowcount = this.size.get();
            if (nowcount < corePoolSize) {
                //小于核心线程数
                if (this.addWorker(runnable, true)) {
                    //成功添加线程
                    return;
                }
            }
            //添加到任务队列
            if (status == RUNNING && this.workQueue.offer(runnable)) {
                if (status != RUNNING && this.remove(runnable)) {
                    //线程池状态改变，移除队列中的任务，走拒绝策略
                    this.rejectedHandler.rejected(runnable, this);
                }
                //任务队列以满，尝试添加应急线程
            } else if (!this.addWorker(runnable, false)) {
                //任务队列满创建应急线程失败 走拒绝策略
                this.rejectedHandler.rejected(runnable, this);
            }
        }
    }


    private boolean remove(Runnable runnable) {
        //  从任务队列中移除任务
        boolean remove = this.workQueue.remove(runnable);
        return remove;
    }

    private boolean addWorker(Runnable runnable, boolean b) {
        while (true) {
            int sizenum = this.size.get();
            if (status != RUNNING) {
                return false;
            }
            while (true) {
                if (sizenum >= (b ? corePoolSize : maxPoolSize)) {
                    return false;
                }

                if (this.compareAndIncrementWorkerCount(sizenum)) {
                    Worker worker = null;
                    try {
                        if (!b) {
                            worker = new Worker(runnable, true, timeUnit, keepAliveTime);
                        } else {
                            worker = new Worker(runnable);
                        }
                        if (worker != null) {
                            ReentrantLock lock = this.mainLock;
                            lock.lock();
                            try {
                                if (status == RUNNING) {
                                    workers.add(worker);
                                    if (workers.size() > this.largestPoolSize) {
                                        largestPoolSize = workers.size();
                                    }
                                }
                            } finally {
                                lock.unlock();
                            }
                            worker.thread.start();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            }
        }
    }

    private boolean compareAndIncrementWorkerCount(int sizenum) {
        return this.size.compareAndSet(sizenum, sizenum + 1);
    }

    public static class CallerRuns implements RejectedHandler {
        @Override
        public void rejected(Runnable runnable, MyThreadPool myThreadPoll) {
            if (!myThreadPoll.isShutdown()) {
                runnable.run();
            }
        }
    }

    public static class DisRuns implements RejectedHandler {
        @Override
        public void rejected(Runnable runnable, MyThreadPool myThreadPoll) {
            /*throw new RuntimeException("线程池已满 拒绝任务");*/
            System.out.println("线程池已满 拒绝任务");
        }
    }

    public void shutdown() {
        System.out.println("关闭线程池");
        ReentrantLock lock = this.mainLock;
        lock.lock();
        try {
            //修改状态为不接受任务
            this.status = this.SHUTDOWN;
            //遍历所有线程 不执行任务的终端
            this.interruptWorkers();
            //终止检查，如果任务队列为null 状态设置stop
            if (this.workQueue == null) {
                this.status = this.STOP;
            }
        } finally {
            lock.unlock();
        }

    }

    private void interruptWorkers() {
        ReentrantLock lock = this.mainLock;
        lock.lock();
        try {
            for (Worker worker : workers) {
                worker.changeStatus();
            }
        } finally {
            lock.unlock();
        }
    }



    private boolean isShutdown() {
        return this.status == SHUTDOWN;
    }


    private void throwThread() {
        int andDecrement = this.size.getAndDecrement();
    }

    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {

        final Thread thread;
        Runnable firstTask;

        boolean isTemp;

        long AliveTime;
        TimeUnit timeUnit;
        long lastRunTime;

        boolean status;

        AtomicInteger lock;

        public boolean trylock(){
            if (lock==null){
                lock=new AtomicInteger(0);
            }
            if (lock.get()==1){
                return false;
            }
           return lock.compareAndSet(0,1);
        }

        public boolean tryunlock(){
            if (lock.get()!=1){
                return false;
            }
            return lock.compareAndSet(1,0);
        }


        public Worker(Runnable runnable) {
            this.thread = MyThreadPool.this.threadFactory.newThread(this);
            this.firstTask = runnable;
            this.isTemp = false;
            AliveTime = 0;
            this.timeUnit = null;
            this.lastRunTime = 0;
            this.status = true;
        }

        public Worker(Runnable runnable,
                      boolean isTemp, TimeUnit timeUnit,
                      long aliveTime) {
            this.thread = MyThreadPool.this.threadFactory.newThread(this);
            this.firstTask = runnable;
            this.isTemp = isTemp;
            AliveTime = aliveTime;
            this.timeUnit = timeUnit;
            this.lastRunTime = System.currentTimeMillis();
            this.status = true;
        }

        public void changeStatus(){
            try{
                while (!trylock()){
                    sleep(1000);
                }
                this.status=false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                tryunlock();
            }
        }

        public boolean isTooOld(){
            long now = System.currentTimeMillis();
            if (now - this.lastRunTime >= this.timeUnit.toMillis(this.AliveTime)) {
                //超时
                throwThread();
                status=false;
                return true;
            } else {
                return false;
            }
        }


        @Override
        public void run() {
            firstTask.run();
            while (status) {
                if (this.isTemp) {
                    boolean tooOld = isTooOld();
                    if (tooOld) {
                        return;
                    }else {
                        firstTask= workQueue.get();
                        if (firstTask==null){
                            try {
                                sleep(1000*6);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }
                        lastRunTime=System.currentTimeMillis();
                    }
                }else {
                    firstTask= workQueue.take();
                }
                try {
                    trylock();
                    firstTask.run();
                }finally {
                    tryunlock();
                }
            }
        }
    }

}
