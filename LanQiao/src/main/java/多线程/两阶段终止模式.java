package 多线程;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：两阶段终止模式
 * @Date：2025/4/19 11:20
 * @Filename：两阶段终止模式
 */
@Slf4j
public class 两阶段终止模式 {

    public static void main(String[] args) throws InterruptedException {
        text();
    }

    public static void text() throws InterruptedException {
        Montor montor = new Montor();
        montor.start();
        Thread.sleep(1000*9);
        montor.stop();
    }

    static class  Montor{
        private Thread montorThread;

        public void start(){
            log.debug("启动监控线程");
            montorThread = new Thread(()->{
                while (true){
                    Thread thread = Thread.currentThread();
                    if (thread.isInterrupted()){
                        log.debug("料理后事");
                        break;
                    }else {
                        try {
                            Thread.sleep(1000*2);
                            log.debug("执行监控记录");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            //sleep出现异常后
                            //sleep被打断，恢复打断标记
                            thread.interrupt();
                        }
                    }
                }
            });
            montorThread.start();
        }

        public void stop(){
            montorThread.interrupt();
        }
    }

}
